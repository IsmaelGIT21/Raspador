package br.edu.utfpr.td.tsi;

import br.edu.utfpr.td.tsi.interfaces.Carregador;
import br.edu.utfpr.td.tsi.interfaces.Extrator;
import br.edu.utfpr.td.tsi.interfaces.Transformador;

import java.util.ArrayList;
import java.util.List;

public class Job<T, O> {

    private Extrator<T> extrator;
    private Transformador<T, O> transformador;
    private Carregador<O> carregador;

    public Job() {
    }

    public Job(Extrator<T> extrator, Transformador<T, O> transformador, Carregador<O> carregador) {
        this.extrator = extrator;
        this.transformador = transformador;
        this.carregador = carregador;
    }

    public void executar() {
        List<O> dadosTransformados = new ArrayList<>();
        List<T> listaDadosBrutos = extrator.extrair();

        for (T dadosBruto : listaDadosBrutos) {
            O dadoTransformado = transformador.transformar(dadosBruto);
            if (dadoTransformado != null) {
                dadosTransformados.add(dadoTransformado);
            }
        }

        carregador.carregar(dadosTransformados);
    }

    public void setExtrator(Extrator<T> extrator) {
        this.extrator = extrator;
    }

    public void setTransformador(Transformador<T, O> transformador) {
        this.transformador = transformador;
    }

    public void setCarregador(Carregador<O> carregador) {
        this.carregador = carregador;
    }
}
