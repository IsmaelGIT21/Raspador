```java
package br.edu.utfpr.td.tsi.cfm.raspagem;

import br.edu.utfpr.td.tsi.cfm.modelo.DetalheMedicoBruto;
import br.edu.utfpr.td.tsi.cfm.modelo.MedicoBruto;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class RaspadorCFM {

    private static final String URL_BUSCA = "https://portal.cfm.org.br/busca-medicos/";
    private static final int TAMANHO_PAGINA = 10;

    /** Intercepta as chamadas AJAX da página e guarda as respostas da API em window.__cfm. */
    private static final String SCRIPT_INTERCEPTADOR = """
            if (!window.__cfmHook) {
              window.__cfmHook = true;
              window.__cfm = [];
              const open = XMLHttpRequest.prototype.open;
              const send = XMLHttpRequest.prototype.send;
              XMLHttpRequest.prototype.open = function (metodo, url) {
                this.__url = url;
                return open.apply(this, arguments);
              };
              XMLHttpRequest.prototype.send = function () {
                this.addEventListener('load', () => {
                  if (/buscar_medicos|buscar_foto/.test(this.__url)) {
                    window.__cfm.push({ url: this.__url, corpo: this.responseText });
                  }
                });
                return send.apply(this, arguments);
              };
            }
            """;

    private final Logger logger = LoggerFactory.getLogger(RaspadorCFM.class);

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)
            .create();

    @Value("${cfm.paginas-por-uf:5}")
    private int paginasPorUf;

    @Value("${cfm.intervalo-entre-paginas-ms:2500}")
    private long intervaloEntrePaginasMs;

    @Value("${cfm.timeout-captcha-segundos:180}")
    private long timeoutCaptchaSegundos;

    public void raspar(List<String> ufs, Consumer<List<MedicoBruto>> consumidor) {
        WebDriver driver = criarDriver();
        try {
            for (String uf : ufs) {
                rasparUf(driver, uf.trim().toUpperCase(), consumidor);
            }
        } finally {
            driver.quit();
        }
    }

    private WebDriver criarDriver() {
        ChromeOptions opcoes = new ChromeOptions();
        opcoes.addArguments("--start-maximized", "--lang=pt-BR");

        try {
            java.net.URL resource = getClass().getClassLoader().getResource("buster.crx");

            if (resource != null) {
                java.io.File arquivoExtensao = org.springframework.util.ResourceUtils.getFile(resource);
                opcoes.addExtensions(arquivoExtensao);
                logger.info("Extensão Buster acoplada com sucesso a partir dos recursos!");
            } else {
                logger.error("ERRO: O arquivo buster.crx não foi encontrado em src/main/resources/");
            }

        } catch (Exception e) {
            logger.error("Erro ao carregar a extensão Buster: ", e);
        }

        return new ChromeDriver(opcoes);
    }

    private void rasparUf(WebDriver driver, String uf, Consumer<List<MedicoBruto>> consumidor) {
        logger.info("Iniciando raspagem da UF {} ({} página(s))", uf, paginasPorUf);
        abrirPaginaDeBusca(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("""
                jQuery('#nome').val('');
                jQuery('#crm').val('');
                jQuery('#uf').val(arguments[0]).trigger('change');
                jQuery('#buscaForm').submit();
                """, uf);

        for (int pagina = 1; pagina <= paginasPorUf; pagina++) {
            if (pagina > 1) {
                pausar(intervaloEntrePaginasMs);

                boolean existe = (Boolean) js.executeScript(
                        "const li = jQuery('#paginacao li[data-num=' + arguments[0] + ']');"
                                + "if (!li.length) return false; li.click(); return true;", pagina);

                if (!existe) {
                    logger.info("UF {}: não há mais páginas após a página {}", uf, pagina - 1);
                    break;
                }
            }

            List<MedicoBruto> medicos = aguardarPagina(js, uf, pagina);

            if (medicos.isEmpty()) {
                logger.warn("UF {}: página {} sem resultados, encerrando esta UF", uf, pagina);
                break;
            }

            logger.info("UF {}: página {} -> {} médico(s)", uf, pagina, medicos.size());
            consumidor.accept(medicos);
        }
    }

    private void abrirPaginaDeBusca(WebDriver driver) {
        driver.get(URL_BUSCA);

        new WebDriverWait(driver, Duration.ofSeconds(60)).until(d -> (Boolean) ((JavascriptExecutor) d).executeScript(
                "return typeof jQuery !== 'undefined' && jQuery('#uf option').length > 1"
                        + " && typeof grecaptcha !== 'undefined' && typeof grecaptcha.execute === 'function';"));

        ((JavascriptExecutor) driver).executeScript(SCRIPT_INTERCEPTADOR);
    }

    private List<MedicoBruto> aguardarPagina(JavascriptExecutor js, String uf, int pagina) {
        int primeiroRegistro = (pagina - 1) * TAMANHO_PAGINA + 1;
        long inicio = System.currentTimeMillis();
        long limite = inicio + timeoutCaptchaSegundos * 1000;
        boolean avisouCaptcha = false;

        WebDriver driver = (WebDriver) js;

        List<MedicoBruto> medicos = null;
        Map<String, DetalheMedicoBruto> detalhes = new HashMap<>();
        Long momentoMedicos = null;

        while (System.currentTimeMillis() < limite) {

            avisouCaptcha = tentarAcionarBuster(driver, avisouCaptcha);

            for (JsonObject resposta : lerRespostas(js)) {
                String url = resposta.get("url").getAsString();
                JsonArray dados = extrairDados(resposta.get("corpo").getAsString());

                if (dados == null) {
                    continue;
                }

                if (url.contains("buscar_medicos")) {
                    if (dados.isEmpty()) {
                        return List.of();
                    }

                    int rnum = dados.get(0).getAsJsonObject().get("RNUM").getAsInt();

                    if (rnum == primeiroRegistro) {
                        medicos = converterMedicos(dados);
                        momentoMedicos = System.currentTimeMillis();
                    }

                } else if (url.contains("buscar_foto")) {
                    for (JsonElement elemento : dados) {
                        DetalheMedicoBruto detalhe = gson.fromJson(elemento, DetalheMedicoBruto.class);
                        detalhes.put(detalhe.getUfCrm() + "-" + detalhe.getCrm(), detalhe);
                    }
                }
            }

            if (medicos != null) {
                boolean todosDetalhes = medicos.stream()
                        .allMatch(m -> detalhes.containsKey(m.getId()));

                if (todosDetalhes || System.currentTimeMillis() - momentoMedicos > 15_000) {
                    medicos.forEach(m -> m.setDetalhes(detalhes.get(m.getId())));
                    return medicos;
                }
            }

            if (!avisouCaptcha && System.currentTimeMillis() - inicio > 20_000) {
                logger.warn("A resposta do portal está demorando. Se houver um desafio do reCAPTCHA na janela "
                        + "do Chrome, resolva-o manualmente (aguardando até {}s).", timeoutCaptchaSegundos);
                avisouCaptcha = true;
            }

            pausar(500);
        }

        logger.error("UF {}: tempo esgotado aguardando a página {}", uf, pagina);
        return medicos == null ? List.of() : medicos;
    }

    private List<JsonObject> lerRespostas(JavascriptExecutor js) {
        String json = (String) js.executeScript(
                "const r = window.__cfm || []; window.__cfm = []; return JSON.stringify(r);");

        List<JsonObject> respostas = new ArrayList<>();

        JsonParser.parseString(json)
                .getAsJsonArray()
                .forEach(e -> respostas.add(e.getAsJsonObject()));

        return respostas;
    }

    private JsonArray extrairDados(String corpo) {
        try {
            JsonElement elemento = JsonParser.parseString(corpo);

            if (!elemento.isJsonObject()) {
                logger.debug("Resposta sem dados do portal: {}", corpo);
                return null;
            }

            JsonObject objeto = elemento.getAsJsonObject();

            if (!objeto.has("dados") || !objeto.get("dados").isJsonArray()) {
                return null;
            }

            return objeto.getAsJsonArray("dados");

        } catch (RuntimeException e) {
            logger.debug("Resposta inválida do portal: {}", corpo);
            return null;
        }
    }

    private List<MedicoBruto> converterMedicos(JsonArray dados) {
        List<MedicoBruto> medicos = new ArrayList<>();
        Date agora = new Date();

        for (JsonElement elemento : dados) {
            MedicoBruto medico = gson.fromJson(elemento, MedicoBruto.class);
            medico.setId(medico.gerarId());
            medico.setDataColeta(agora);
            medicos.add(medico);
        }

        return medicos;
    }

    private void pausar(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Raspagem interrompida", e);
        }
    }

    private boolean tentarAcionarBuster(WebDriver driver, boolean avisouCaptcha) {
        try {
            List<org.openqa.selenium.WebElement> iframes = driver.findElements(
                    org.openqa.selenium.By.xpath(
                            "//iframe[contains(@title, 'desafio') or contains(@src, 'bframe')]"));

            if (!iframes.isEmpty() && iframes.get(0).isDisplayed()) {
                if (!avisouCaptcha) {
                    logger.info("Dê um clique no botão verde do Buster na janela do Chrome para destravar o robô.");
                    avisouCaptcha = true;
                }

                pausar(2000);
            } else {
                avisouCaptcha = false;
            }

        } catch (Exception e) {
        }

        return avisouCaptcha;
    }
}
```
