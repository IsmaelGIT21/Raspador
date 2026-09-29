package br.edu.utfpr.td.tsi.mongo;

import br.edu.utfpr.td.tsi.interfaces.Carregador;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class MongoCarregador<T> extends Carregador<T> {

    private MongoTemplate mongoTemplate;

    public MongoCarregador() {
    }

    public MongoCarregador(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void carregar(List<T> lista) {
        lista.forEach(item -> mongoTemplate.save(item));
    }
}
