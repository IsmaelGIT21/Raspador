package br.edu.utfpr.td.tsi.cfm.modelo;

import com.google.gson.annotations.SerializedName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Dado bruto de um médico, exatamente como devolvido pelo endpoint
 * /medicos/buscar_medicos do portal do CFM (os nomes dos campos seguem as
 * chaves do JSON, ex.: NM_MEDICO -> nmMedico).
 */
@Document(collection = "medicos-brutos")
public class MedicoBruto {

    @Id
    private String id;

    private String sgUf;
    private String nuCrm;
    private String nuCrmNatural;
    private String nmMedico;
    private String nmSocial;
    private String codSituacao;
    private String situacao;
    private String dtInscricao;
    private String primInscricaoUf;
    private String inTipoInscricao;
    private String tipoInscricao;
    private String especialidade;
    private String nmInstituicaoGraduacao;
    private String nmFaculdadeEstrangeiraGraduacao;
    private String dtGraduacao;
    private String idTipoFormacao;
    private String hasPosGraduacao;
    @SerializedName("SECURITYHASH")
    private String securityHash;

    /** Complemento devolvido pelo endpoint /medicos/buscar_foto (endereço, telefone, outras inscrições). */
    private DetalheMedicoBruto detalhes;

    private Date dataColeta;

    public String gerarId() {
        return sgUf + "-" + nuCrmNatural;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSgUf() { return sgUf; }
    public void setSgUf(String sgUf) { this.sgUf = sgUf; }
    public String getNuCrm() { return nuCrm; }
    public void setNuCrm(String nuCrm) { this.nuCrm = nuCrm; }
    public String getNuCrmNatural() { return nuCrmNatural; }
    public void setNuCrmNatural(String nuCrmNatural) { this.nuCrmNatural = nuCrmNatural; }
    public String getNmMedico() { return nmMedico; }
    public void setNmMedico(String nmMedico) { this.nmMedico = nmMedico; }
    public String getNmSocial() { return nmSocial; }
    public void setNmSocial(String nmSocial) { this.nmSocial = nmSocial; }
    public String getCodSituacao() { return codSituacao; }
    public void setCodSituacao(String codSituacao) { this.codSituacao = codSituacao; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getDtInscricao() { return dtInscricao; }
    public void setDtInscricao(String dtInscricao) { this.dtInscricao = dtInscricao; }
    public String getPrimInscricaoUf() { return primInscricaoUf; }
    public void setPrimInscricaoUf(String primInscricaoUf) { this.primInscricaoUf = primInscricaoUf; }
    public String getInTipoInscricao() { return inTipoInscricao; }
    public void setInTipoInscricao(String inTipoInscricao) { this.inTipoInscricao = inTipoInscricao; }
    public String getTipoInscricao() { return tipoInscricao; }
    public void setTipoInscricao(String tipoInscricao) { this.tipoInscricao = tipoInscricao; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public String getNmInstituicaoGraduacao() { return nmInstituicaoGraduacao; }
    public void setNmInstituicaoGraduacao(String nmInstituicaoGraduacao) { this.nmInstituicaoGraduacao = nmInstituicaoGraduacao; }
    public String getNmFaculdadeEstrangeiraGraduacao() { return nmFaculdadeEstrangeiraGraduacao; }
    public void setNmFaculdadeEstrangeiraGraduacao(String nmFaculdadeEstrangeiraGraduacao) { this.nmFaculdadeEstrangeiraGraduacao = nmFaculdadeEstrangeiraGraduacao; }
    public String getDtGraduacao() { return dtGraduacao; }
    public void setDtGraduacao(String dtGraduacao) { this.dtGraduacao = dtGraduacao; }
    public String getIdTipoFormacao() { return idTipoFormacao; }
    public void setIdTipoFormacao(String idTipoFormacao) { this.idTipoFormacao = idTipoFormacao; }
    public String getHasPosGraduacao() { return hasPosGraduacao; }
    public void setHasPosGraduacao(String hasPosGraduacao) { this.hasPosGraduacao = hasPosGraduacao; }
    public String getSecurityHash() { return securityHash; }
    public void setSecurityHash(String securityHash) { this.securityHash = securityHash; }
    public DetalheMedicoBruto getDetalhes() { return detalhes; }
    public void setDetalhes(DetalheMedicoBruto detalhes) { this.detalhes = detalhes; }
    public Date getDataColeta() { return dataColeta; }
    public void setDataColeta(Date dataColeta) { this.dataColeta = dataColeta; }
}
