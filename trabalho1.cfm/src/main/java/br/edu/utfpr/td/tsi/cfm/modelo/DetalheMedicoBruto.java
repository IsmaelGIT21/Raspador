package br.edu.utfpr.td.tsi.cfm.modelo;

/**
 * Dado bruto devolvido pelo endpoint /medicos/buscar_foto do portal do CFM.
 */
public class DetalheMedicoBruto {

    private String crm;
    private String ufCrm;
    private String situacao;
    private String endereco;
    private String telefone;
    private String inscricao;
    private String autorizacaoImagem;
    private String autorizacaoEndereco;

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getUfCrm() { return ufCrm; }
    public void setUfCrm(String ufCrm) { this.ufCrm = ufCrm; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getInscricao() { return inscricao; }
    public void setInscricao(String inscricao) { this.inscricao = inscricao; }
    public String getAutorizacaoImagem() { return autorizacaoImagem; }
    public void setAutorizacaoImagem(String autorizacaoImagem) { this.autorizacaoImagem = autorizacaoImagem; }
    public String getAutorizacaoEndereco() { return autorizacaoEndereco; }
    public void setAutorizacaoEndereco(String autorizacaoEndereco) { this.autorizacaoEndereco = autorizacaoEndereco; }
}
