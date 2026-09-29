package br.edu.utfpr.td.tsi.cfm;

import br.edu.utfpr.td.tsi.Job;
import br.edu.utfpr.td.tsi.cfm.etl.TransformadorMedico;
import br.edu.utfpr.td.tsi.cfm.modelo.Medico;
import br.edu.utfpr.td.tsi.cfm.modelo.MedicoBruto;
import br.edu.utfpr.td.tsi.cfm.raspagem.RaspadorCFM;
import br.edu.utfpr.td.tsi.mongo.MongoCarregador;
import br.edu.utfpr.td.tsi.mongo.MongoExtrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Trabalho 1 - obtenção e tratamento dos dados públicos de médicos do CFM.
 * <p>
 * Etapa 1 (raspagem): coleta os dados do portal e persiste os dados brutos na coleção "medicos-brutos".
 * Etapa 2 (etl): usa o framework simples.etl (Extrator -> Transformador -> Carregador) para gerar
 * a coleção "medicos" com os dados tratados e normalizados.
 * <p>
 * Uso: java -jar trabalho1-cfm.jar [--cfm.etapa=raspagem|etl|tudo] [--cfm.ufs=PR,SC] [--cfm.paginas-por-uf=5]
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(App.class);

    private final MongoTemplate mongoTemplate;
    private final RaspadorCFM raspador;

    @Value("${cfm.etapa:tudo}")
    private String etapa;

    @Value("${cfm.ufs:PR,SC}")
    private List<String> ufs;

    public App(MongoTemplate mongoTemplate, RaspadorCFM raspador) {
        this.mongoTemplate = mongoTemplate;
        this.raspador = raspador;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
        if (etapa.equals("raspagem") || etapa.equals("tudo")) {
            executarRaspagem();
        }
        if (etapa.equals("etl") || etapa.equals("tudo")) {
            executarEtl();
        }
    }

    private void executarRaspagem() {
        logger.info("=== Etapa 1: raspagem do portal do CFM (UFs: {}) ===", ufs);
        // save() faz upsert pelo id (UF-CRM), então reexecutar não duplica registros
        raspador.raspar(ufs, pagina -> pagina.forEach(mongoTemplate::save));
        logger.info("Dados brutos armazenados: {}", mongoTemplate.count(new Query(), MedicoBruto.class));
    }

    private void executarEtl() {
        logger.info("=== Etapa 2: ETL dados brutos -> dados transformados ===");
        mongoTemplate.dropCollection(Medico.class);

        Job<MedicoBruto, Medico> job = new Job<>(
                new MongoExtrator<>(mongoTemplate, MedicoBruto.class),
                new TransformadorMedico(),
                new MongoCarregador<>(mongoTemplate));
        job.executar();

        logger.info("Médicos transformados: {}", mongoTemplate.count(new Query(), Medico.class));
    }
}
