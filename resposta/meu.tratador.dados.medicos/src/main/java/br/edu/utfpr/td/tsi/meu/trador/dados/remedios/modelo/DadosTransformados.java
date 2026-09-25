package br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "dados-transformados")
public class DadosTransformados {
    @Id
    private String id;
    
    private Long numeroRegistro;
    private String numeroProcesso;
    private String situacaoRegistro;
    private Date dataFinalizacao;
    private Date dataVencimento;

    
    private Produto produto;
    private Empresa empresa;

    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(Long numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public String getNumeroProcesso() { return numeroProcesso; }
    public void setNumeroProcesso(String numeroProcesso) { this.numeroProcesso = numeroProcesso; }

    public String getSituacaoRegistro() { return situacaoRegistro; }
    public void setSituacaoRegistro(String situacaoRegistro) { this.situacaoRegistro = situacaoRegistro; }

    public Date getDataFinalizacao() { return dataFinalizacao; }
    public void setDataFinalizacao(Date dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }

    public Date getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(Date dataVencimento) { this.dataVencimento = dataVencimento; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}
