# Trabalho 1 — Médicos do CFM

Obtém os dados públicos de médicos do portal do CFM (https://portal.cfm.org.br/busca-medicos),
persiste os dados brutos no MongoDB e os trata com um processo de ETL construído sobre o
framework `simples.etl` desenvolvido em aula.

## Fluxo

```
Portal CFM ──(Selenium + Chrome)──> RaspadorCFM ──> Mongo "medicos-brutos"      (etapa 1: raspagem)
Mongo "medicos-brutos" ──> MongoExtrator ──> TransformadorMedico ──> MongoCarregador ──> Mongo "medicos"   (etapa 2: ETL / Job)
```

## Estrutura

| Classe | Papel |
|---|---|
| `App` | Ponto de entrada (Spring Boot). Executa a raspagem e/ou o ETL. |
| `raspagem.RaspadorCFM` | Controla o Chrome, preenche a busca por UF, pagina e captura o JSON das APIs `buscar_medicos` e `buscar_foto` do portal. |
| `modelo.MedicoBruto` / `DetalheMedicoBruto` | Dado bruto, com os mesmos campos da API do portal. |
| `modelo.Medico` / `Especialidade` / `Formacao` | Dado transformado (formato pedido no enunciado). |
| `etl.TransformadorMedico` | Extends `Transformador<MedicoBruto, Medico>` do framework: normaliza nome, CRM, UF, separa especialidades/RQE, instituição/campus e ano de graduação. |
| `simples.etl`: `Job`, `MongoExtrator`, `MongoCarregador` | Framework de ETL reutilizado. |

## Por que Selenium

A busca do portal é protegida por reCAPTCHA (invisível). Por isso o sistema usa um navegador real
e o próprio formulário da página. Normalmente o captcha passa sozinho; se o Google mostrar um
desafio, basta resolvê-lo na janela do Chrome — o raspador espera (até `cfm.timeout-captcha-segundos`).

## Requisitos

- Java 17+
- MongoDB em `localhost:27017` (banco `cfm`)
- Google Chrome instalado (o driver é baixado automaticamente pelo Selenium Manager)

## Build e execução

```bash
cd ../simples.etl && mvn install
cd ../trabalho1.cfm && mvn package
java -jar target/trabalho1-cfm.jar
```

Opções (em `application.properties` ou na linha de comando):

```bash
java -jar target/trabalho1-cfm.jar --cfm.ufs=PR,SC --cfm.paginas-por-uf=10
java -jar target/trabalho1-cfm.jar --cfm.etapa=raspagem
java -jar target/trabalho1-cfm.jar --cfm.etapa=etl
```

Cada página tem 10 médicos. A raspagem grava por página e usa `UF-CRM` como id, então pode ser
reexecutada sem duplicar. O ETL recria a coleção `medicos` a cada execução.

## Exemplo de resultado (coleção `medicos`)

```json
{
  "nome": "ABDIAS BAPTISTA DE MELLO NETO",
  "crm": "26255",
  "estado": "SC",
  "especialidades": [
    { "descricao": "CLÍNICA MÉDICA", "rqe": "16673" },
    { "descricao": "PNEUMOLOGIA", "rqe": "16674" }
  ],
  "formacao": { "instituicao": "UNIVERSIDADE FEDERAL DE SANTA MARIA", "anoGraduacao": 2006 },
  "situacao": "Cancelado"
}
```
