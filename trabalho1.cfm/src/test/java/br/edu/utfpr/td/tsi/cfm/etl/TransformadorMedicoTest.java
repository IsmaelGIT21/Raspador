package br.edu.utfpr.td.tsi.cfm.etl;

import br.edu.utfpr.td.tsi.cfm.modelo.Medico;
import br.edu.utfpr.td.tsi.cfm.modelo.MedicoBruto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformadorMedicoTest {

    private final TransformadorMedico transformador = new TransformadorMedico();

    @Test
    void transformaMedicoComEspecialidadesECampus() {
        MedicoBruto bruto = new MedicoBruto();
        bruto.setNmMedico(" BRUNO  HENRIQUE ALVES ");
        bruto.setNuCrm("7312");
        bruto.setSgUf("PR");
        bruto.setSituacao("Regular");
        bruto.setEspecialidade("&CIRURGIA GERAL - RQE N.: 3497  &CIRURGIA VASCULAR - RQE N.: 3778  / Angiorradiologia e Cirurgia Endovascular ");
        bruto.setNmInstituicaoGraduacao("UNIVERSIDADE FEDERAL DO PARANÁ - CAMPUS COLOMBO");
        bruto.setDtGraduacao("2008");

        Medico medico = transformador.transformar(bruto);

        assertEquals("BRUNO HENRIQUE ALVES", medico.getNome());
        assertEquals("7312", medico.getCrm());
        assertEquals("PR", medico.getEstado());
        assertEquals("Regular", medico.getSituacao());
        assertEquals(2, medico.getEspecialidades().size());
        assertEquals("CIRURGIA GERAL", medico.getEspecialidades().get(0).getDescricao());
        assertEquals("3497", medico.getEspecialidades().get(0).getRqe());
        assertEquals("CIRURGIA VASCULAR", medico.getEspecialidades().get(1).getDescricao());
        assertEquals("3778", medico.getEspecialidades().get(1).getRqe());
        assertEquals("UNIVERSIDADE FEDERAL DO PARANÁ", medico.getFormacao().getInstituicao());
        assertEquals("Colombo", medico.getFormacao().getCampus());
        assertEquals(2008, medico.getFormacao().getAnoGraduacao());
    }

    @Test
    void medicoSemEspecialidadeESemCampus() {
        MedicoBruto bruto = new MedicoBruto();
        bruto.setNmMedico("MARIA SILVA");
        bruto.setNuCrm("123");
        bruto.setSgUf("SC");
        bruto.setEspecialidade(null);
        bruto.setNmInstituicaoGraduacao("UNIVERSIDADE FEDERAL DE SANTA CATARINA");
        bruto.setDtGraduacao("15/12/1999");

        Medico medico = transformador.transformar(bruto);

        assertTrue(medico.getEspecialidades().isEmpty());
        assertEquals("UNIVERSIDADE FEDERAL DE SANTA CATARINA", medico.getFormacao().getInstituicao());
        assertNull(medico.getFormacao().getCampus());
        assertEquals(1999, medico.getFormacao().getAnoGraduacao());
    }

    @Test
    void instituicaoComSede() {
        MedicoBruto bruto = new MedicoBruto();
        bruto.setNmMedico("ABELARDO VIANNA FILHO");
        bruto.setNuCrm("571");
        bruto.setSgUf("SC");
        bruto.setNmInstituicaoGraduacao("UNIVERSIDADE FEDERAL DE SANTA CATARINA - SEDE");
        bruto.setDtGraduacao("1965");

        Medico medico = transformador.transformar(bruto);

        assertEquals("UNIVERSIDADE FEDERAL DE SANTA CATARINA", medico.getFormacao().getInstituicao());
        assertEquals("Sede", medico.getFormacao().getCampus());
    }

    @Test
    void campusComPreposicoes() {
        assertEquals("São José dos Pinhais", transformador.capitalizar("SÃO JOSÉ DOS PINHAIS"));
    }

    @Test
    void formadoNoExteriorUsaFaculdadeEstrangeira() {
        MedicoBruto bruto = new MedicoBruto();
        bruto.setNmMedico("JOSE PEREZ");
        bruto.setNuCrm("999");
        bruto.setSgUf("PR");
        bruto.setNmFaculdadeEstrangeiraGraduacao("UNIVERSIDAD DE BUENOS AIRES");
        bruto.setNmInstituicaoGraduacao("UNIVERSIDADE FEDERAL DO PARANÁ");
        bruto.setDtGraduacao("2010");

        Medico medico = transformador.transformar(bruto);

        assertEquals("UNIVERSIDAD DE BUENOS AIRES", medico.getFormacao().getInstituicao());
    }

    @Test
    void registroSemNomeEhDescartado() {
        assertNull(transformador.transformar(new MedicoBruto()));
    }
}
