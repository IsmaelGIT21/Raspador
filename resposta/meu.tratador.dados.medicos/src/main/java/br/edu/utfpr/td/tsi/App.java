package br.edu.utfpr.td.tsi;

import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.DadosBrutos;
import br.edu.utfpr.td.tsi.meu.trador.dados.medicos.modelo.Medico;
import br.edu.utfpr.td.tsi.minha.etl.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private MongoRepository dadosBrutosRepository;

    @Autowired
    private MongoRepository dadosTransformadosRepository;

    @PostConstruct
    public void init() {
        ExtratorMongo<DadosBrutos, String> meuExtrator = new ExtratorMongo();
        Transformador<DadosBrutos, Medico> meuTransofrmador = new MeuTransformador();
        CarregadorMongoDB<Medico, String> meuCarregador = new CarregadorMongoDB();

        meuExtrator.setRepository(dadosBrutosRepository);
        meuCarregador.setRepository(dadosTransformadosRepository);

        Job<DadosBrutos, Medico> meuJob = new Job();
        meuJob.setCarregador(meuCarregador);
        meuJob.setTransformador(meuTransofrmador);
        meuJob.setExtrator(meuExtrator);

        meuJob.executar();
    }
}
