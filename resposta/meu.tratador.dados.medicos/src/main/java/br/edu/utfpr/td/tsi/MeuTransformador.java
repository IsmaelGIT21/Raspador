package br.edu.utfpr.td.tsi;

import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.DadosBrutos;
import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.Especialidade;
import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.Formacao;
import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.Medico;
import br.edu.utfpr.td.tsi.minha.etl.Transformador;

import java.util.ArrayList;
import java.util.List;

public class MeuTransformador extends Transformador<DadosBrutos, Medico> {

    public Medico transformar(DadosBrutos bruto) {
        Medico transformado = new Medico();

        transformado.setId(bruto.getId());
        transformado.setNome(bruto.getNome());
        transformado.setSituacao(bruto.getSituacao());

        String[] crm = separarCrm(bruto.getCrm(), bruto.getEstado());
        transformado.setCrm(crm[0]);
        transformado.setEstado(crm[1]);

        Formacao formacao = new Formacao();
        formacao.setAnoGraduacao(obterAnoGraduacao(bruto.getDataGraduacao()));
        separarInstituicao(bruto.getInstituicaoGraduacao(), formacao);
        transformado.setFormacao(formacao);

        transformado.setEspecialidades(separarEspecialidades(bruto.getEspecialidade()));

        return transformado;
    }

    private String[] separarCrm(String valorCrm, String estado) {
        if (valorCrm == null || valorCrm.isBlank()) {
            return new String[]{null, estado};
        }

        String[] partes = valorCrm.split("/", 2);

        String crm = partes[0].trim();
        String uf = partes.length > 1 ? partes[1].trim() : estado;

        return new String[]{crm, uf};
    }

    private int obterAnoGraduacao(String valor) {
        if (valor == null || valor.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void separarInstituicao(String valor, Formacao formacao) {
        if (valor == null || valor.isBlank()) {
            return;
        }

        String[] partes = valor.split("\\s+-\\s+", 2);

        formacao.setInstituicao(partes[0].trim());

        if (partes.length > 1) {
            formacao.setCampus(partes[1].trim());
        }
    }

    private List<Especialidade> separarEspecialidades(String valor) {
        List<Especialidade> especialidades = new ArrayList<>();

        if (valor == null || valor.isBlank()) {
            return especialidades;
        }

        String[] itens = valor.split(";");

        for (String item : itens) {
            String texto = item.trim();

            if (texto.isBlank()) {
                continue;
            }

            Especialidade especialidade = new Especialidade();

            String[] partes = texto.split("\\s+-\\s+RQE\\s+N\\.:\\s*", 2);

            especialidade.setDescricao(partes[0].trim());

            if (partes.length > 1) {
                especialidade.setRqe(partes[1].trim());
            }

            especialidades.add(especialidade);
        }

        return especialidades;
    }
}