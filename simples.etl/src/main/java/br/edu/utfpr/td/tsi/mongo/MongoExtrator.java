package br.edu.utfpr.td.tsi.mongo;

import br.edu.utfpr.td.tsi.interfaces.Extrator;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class MongoExtrator<T> extends Extrator<T> {

    private MongoTemplate mongoTemplate;
    private Class<T> clazz;

    public MongoExtrator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public MongoExtrator(MongoTemplate mongoTemplate, Class<T> clazz) {
        this.mongoTemplate = mongoTemplate;
        this.clazz = clazz;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<T> extrair() {
        return mongoTemplate.findAll(clazz);
    }
}
