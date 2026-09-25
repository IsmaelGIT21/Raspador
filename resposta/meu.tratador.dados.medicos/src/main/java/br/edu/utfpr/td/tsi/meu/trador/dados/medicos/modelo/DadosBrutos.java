package br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dados-brutos")
public class DadosBrutos {
   @Id
   private String id;

   private String nome;
   private String crm;
   private String estado;
   private String especialidade;
   private String dataGraduacao;
   private String instituicaoGraduacao;
   private String situacao;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getCrm() {
      return crm;
   }

   public void setCrm(String crm) {
      this.crm = crm;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getEspecialidade() {
      return especialidade;
   }

   public void setEspecialidade(String especialidade) {
      this.especialidade = especialidade;
   }

   public String getDataGraduacao() {
      return dataGraduacao;
   }

   public void setDataGraduacao(String dataGraduacao) {
      this.dataGraduacao = dataGraduacao;
   }

   public String getInstituicaoGraduacao() {
      return instituicaoGraduacao;
   }

   public void setInstituicaoGraduacao(String instituicaoGraduacao) {
      this.instituicaoGraduacao = instituicaoGraduacao;
   }

   public String getSituacao() {
      return situacao;
   }

   public void setSituacao(String situacao) {
      this.situacao = situacao;
   }
}