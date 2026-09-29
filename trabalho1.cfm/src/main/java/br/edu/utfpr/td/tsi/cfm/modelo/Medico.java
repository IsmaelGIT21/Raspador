package br.edu.utfpr.td.tsi.cfm.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Médico após o tratamento e a normalização (resultado final do ETL).
 */
@Document(collection = "medicos")
public class Medico {

    @Id
    private String id;

    private String nome;
    private String crm;
    private String estado;
    private List<Especialidade> especialidades = new ArrayList<>();
    private Formacao formacao;
    private String situacao;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<Especialidade> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<Especialidade> especialidades) { this.especialidades = especialidades; }
    public Formacao getFormacao() { return formacao; }
    public void setFormacao(Formacao formacao) { this.formacao = formacao; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
}
