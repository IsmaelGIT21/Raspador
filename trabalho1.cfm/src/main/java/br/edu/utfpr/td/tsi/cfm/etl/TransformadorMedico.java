package br.edu.utfpr.td.tsi.cfm.etl;

import br.edu.utfpr.td.tsi.cfm.modelo.Especialidade;
import br.edu.utfpr.td.tsi.cfm.modelo.Formacao;
import br.edu.utfpr.td.tsi.cfm.modelo.Medico;
import br.edu.utfpr.td.tsi.cfm.modelo.MedicoBruto;
import br.edu.utfpr.td.tsi.interfaces.Transformador;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformadorMedico extends Transformador<MedicoBruto, Medico> {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private static final Pattern ESPECIALIDADE_COM_RQE =
            Pattern.compile("^(.*?)\\s*-\\s*RQE\\s*N\\S*\\s*:?\\s*(\\d+).*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern INSTITUICAO_COM_CAMPUS =
            Pattern.compile("^(.*?)\\s*-\\s*(?:CAMPUS\\s+(.+)|(SEDE))$", Pattern.CASE_INSENSITIVE);

    private static final Pattern ANO = Pattern.compile("(\\d{4})");

    private static final List<String> PALAVRAS_MINUSCULAS = List.of("de", "da", "do", "das", "dos", "e");

    @Override
    public Medico transformar(MedicoBruto bruto) {
        if (bruto == null || vazio(bruto.getNmMedico()) || vazio(bruto.getNuCrm())) {
            return null;
        }

        Medico medico = new Medico();
        medico.setNome(normalizarTexto(bruto.getNmMedico()).toUpperCase(PT_BR));
        medico.setCrm(bruto.getNuCrm().replaceAll("\\D", ""));
        medico.setEstado(normalizarTexto(bruto.getSgUf()).toUpperCase(PT_BR));
        medico.setEspecialidades(transformarEspecialidades(bruto.getEspecialidade()));
        medico.setFormacao(transformarFormacao(bruto));
        medico.setSituacao(vazio(bruto.getSituacao()) ? null : normalizarTexto(bruto.getSituacao()));
        return medico;
    }

    List<Especialidade> transformarEspecialidades(String especialidadesBrutas) {
        List<Especialidade> especialidades = new ArrayList<>();
        if (vazio(especialidadesBrutas)) {
            return especialidades;
        }

        for (String item : especialidadesBrutas.split("&")) {
            String texto = normalizarTexto(item);
            if (texto.isEmpty()) {
                continue;
            }
            Matcher matcher = ESPECIALIDADE_COM_RQE.matcher(texto);
            if (matcher.matches()) {
                especialidades.add(new Especialidade(matcher.group(1).toUpperCase(PT_BR), matcher.group(2)));
            } else {
                String descricao = texto.split("\\s/\\s")[0].trim();
                especialidades.add(new Especialidade(descricao.toUpperCase(PT_BR), null));
            }
        }
        return especialidades;
    }

    Formacao transformarFormacao(MedicoBruto bruto) {
        String instituicaoBruta = !vazio(bruto.getNmFaculdadeEstrangeiraGraduacao())
                ? bruto.getNmFaculdadeEstrangeiraGraduacao()
                : bruto.getNmInstituicaoGraduacao();

        if (vazio(instituicaoBruta) && vazio(bruto.getDtGraduacao())) {
            return null;
        }

        Formacao formacao = new Formacao();
        if (!vazio(instituicaoBruta)) {
            String instituicao = normalizarTexto(instituicaoBruta);
            Matcher matcher = INSTITUICAO_COM_CAMPUS.matcher(instituicao);
            if (matcher.matches()) {
                formacao.setInstituicao(matcher.group(1).toUpperCase(PT_BR));
                String campus = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
                formacao.setCampus(capitalizar(campus));
            } else {
                formacao.setInstituicao(instituicao.toUpperCase(PT_BR));
            }
        }
        formacao.setAnoGraduacao(extrairAno(bruto.getDtGraduacao()));
        return formacao;
    }

    Integer extrairAno(String data) {
        if (vazio(data)) {
            return null;
        }
        Matcher matcher = ANO.matcher(data);
        Integer ano = null;
        while (matcher.find()) {
            ano = Integer.valueOf(matcher.group(1));
        }
        return ano;
    }

    String capitalizar(String texto) {
        String[] palavras = normalizarTexto(texto).toLowerCase(PT_BR).split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palavras.length; i++) {
            String palavra = palavras[i];
            if (palavra.isEmpty()) {
                continue;
            }
            if (i > 0 && PALAVRAS_MINUSCULAS.contains(palavra)) {
                resultado.append(palavra);
            } else {
                resultado.append(palavra.substring(0, 1).toUpperCase(PT_BR)).append(palavra.substring(1));
            }
            if (i < palavras.length - 1) {
                resultado.append(' ');
            }
        }
        return resultado.toString();
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(' ', ' ').replaceAll("\\s+", " ").trim();
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
