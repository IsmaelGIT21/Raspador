package br.edu.utfpr.td.tsi.meu.trador.dados.remedios.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "dados-brutos")
public class DadosBrutos {
   @Id
   private String id;

   @Field("TIPO_PRODUTO")
   private String tipoProduto;

   @Field("NOME_PRODUTO")
   private String nomeProduto;

   @Field("DATA_FINALIZACAO_PROCESSO")
   private String dataFinalizacaoProcesso;

   @Field("CATEGORIA_REGULATORIA")
   private String categoriaRegulatoria;

   @Field("NUMERO_REGISTRO_PRODUTO")
   private Long numeroRegistroProduto;

   @Field("DATA_VENCIMENTO_REGISTRO")
   private String dataVencimentoRegistro;

   @Field("NUMERO_PROCESSO")
   private String numeroProcesso;

   @Field("CLASSE_TERAPEUTICA")
   private String classeTerapeutica;

   @Field("EMPRESA_DETENTORA_REGISTRO")
   private String empresaDetentoraRegistro;

   @Field("SITUACAO_REGISTRO")
   private String situacaoRegistro;

   @Field("PRINCIPIO_ATIVO")
   private String principioAtivo;

   // --- Getters e Setters ---

   public String getId() {
      return id;
   }

   public void setId(String id) {
      classId(id);
   }

   private void classId(String id) {
      this.id = id;
   }

   public String getTipoProduto() {
      return tipoProduto;
   }

   public void setTipoProduto(String tipoProduto) {
      this.tipoProduto = tipoProduto;
   }

   public String getNomeProduto() {
      return nomeProduto;
   }

   public void setNomeProduto(String nomeProduto) {
      this.nomeProduto = nomeProduto;
   }

   public String getDataFinalizacaoProcesso() {
      return dataFinalizacaoProcesso;
   }

   public void setDataFinalizacaoProcesso(String dataFinalizacaoProcesso) {
      this.dataFinalizacaoProcesso = dataFinalizacaoProcesso;
   }

   public String getCategoriaRegulatoria() {
      return categoriaRegulatoria;
   }

   public void setCategoriaRegulatoria(String categoriaRegulatoria) {
      this.categoriaRegulatoria = categoriaRegulatoria;
   }

   public Long getNumeroRegistroProduto() {
      return numeroRegistroProduto;
   }

   public void setNumeroRegistroProduto(Long numeroRegistroProduto) {
      this.numeroRegistroProduto = numeroRegistroProduto;
   }

   public String getDataVencimentoRegistro() {
      return dataVencimentoRegistro;
   }

   public void setDataVencimentoRegistro(String dataVencimentoRegistro) {
      this.dataVencimentoRegistro = dataVencimentoRegistro;
   }

   public String getNumeroProcesso() {
      return numeroProcesso;
   }

   public void setNumeroProcesso(String numeroProcesso) {
      this.numeroProcesso = numeroProcesso;
   }

   public String getClasseTerapeutica() {
      return classeTerapeutica;
   }

   public void setClasseTerapeutica(String classeTerapeutica) {
      this.classeTerapeutica = classeTerapeutica;
   }

   public String getEmpresaDetentoraRegistro() {
      return empresaDetentoraRegistro;
   }

   public void setEmpresaDetentoraRegistro(String empresaDetentoraRegistro) {
      this.empresaDetentoraRegistro = empresaDetentoraRegistro;
   }

   public String getSituacaoRegistro() {
      return situacaoRegistro;
   }

   public void setSituacaoRegistro(String situacaoRegistro) {
      this.situacaoRegistro = situacaoRegistro;
   }

   public String getPrincipioAtivo() {
      return principioAtivo;
   }

   public void setPrincipioAtivo(String principioAtivo) {
      this.principioAtivo = principioAtivo;
   }
}
