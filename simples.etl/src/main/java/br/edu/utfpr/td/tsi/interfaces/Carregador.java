package br.edu.utfpr.td.tsi.interfaces;

import java.util.List;

public abstract class Carregador<T> {

    public abstract void carregar(List<T> items);
}
