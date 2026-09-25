package br.edu.utfpr.td.tsi;

import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.Medico;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DadosTransformadosRepository extends MongoRepository<Medico, String> {
}