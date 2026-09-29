package br.edu.utfpr.td.tsi.cfm.modelo;

public class Especialidade {

    private String descricao;
    private String rqe;

    public Especialidade() {
    }

    public Especialidade(String descricao, String rqe) {
        this.descricao = descricao;
        this.rqe = rqe;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getRqe() { return rqe; }
    public void setRqe(String rqe) { this.rqe = rqe; }
}
