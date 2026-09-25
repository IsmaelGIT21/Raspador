package br.edu.utfpr.td.tsi;

import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.DadosBrutos;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DadosBrutosRepository extends MongoRepository<DadosBrutos, String> {
}