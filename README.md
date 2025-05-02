# Desafio Codecon - Análise de Dados de Usuários

Este repositório contém a solução para o desafio proposto pela Codecon, que envolve o processamento e análise de dados de usuários a partir de um arquivo JSON. A aplicação implementada em Spring Boot oferece uma API RESTful para carregar os dados e realizar diversas consultas e agregações.

## Funcionalidades Implementadas

A API oferece as seguintes funcionalidades:

* **Upload de Dados de Usuários (`POST /users`):** Permite o envio de um arquivo JSON contendo uma lista de objetos de usuário. Os dados carregados são armazenados em memória para processamento posterior. A API realiza verificações básicas, retornando respostas informativas em caso de erro.

* **Listagem de Superusuários (`GET /superusers`):** Retorna uma lista de usuários que possuem um score igual ou superior a 900 e estão ativos (`active = true`). A resposta inclui o timestamp da requisição e o tempo de processamento. Caso nenhum usuário atenda aos critérios ou os dados ainda não tenham sido carregados, a API retorna um erro `400 Bad Request`.

* **Top 5 Países com Mais Usuários (`GET /top-countries`):** Retorna os 5 países com o maior número de usuários ativos com score igual ou superior a 900. A resposta apresenta uma lista ordenada por total de usuários, juntamente com o timestamp e o tempo de processamento. Similarmente aos superusuários, um erro `400 Bad Request` é retornado se os dados não foram carregados.

* **Insights por Time (`GET /team-insights`):** Agrupa os usuários por nome do time e retorna estatísticas para cada time, incluindo o total de membros, o número de líderes, o total de projetos concluídos por membros do time e a porcentagem de membros ativos. A resposta contém o timestamp e o tempo de processamento, e um erro `400 Bad Request` é retornado se os dados não estiverem disponíveis.

* **Contagem de Logins por Data (`GET /active-users-per-day?min=3000` (parâmetro `min` opcional)):** Analisa os logs de cada usuário e conta o número de logins por data. Um parâmetro de query opcional `min` permite filtrar os resultados, exibindo apenas as datas com um número de logins igual ou superior ao valor fornecido. A resposta inclui a data e a contagem de logins, além do timestamp e tempo de processamento. A ausência de dados resulta em um erro `400 Bad Request`.

## Arquitetura da Solução

A aplicação segue uma arquitetura MVC (Model-View-Controller) simplificada, utilizando os seguintes componentes principais:

* **Controller (`UserController`):** Responsável por receber as requisições HTTP, interagir com o `UserService` para realizar a lógica de negócios e retornar as respostas HTTP. Realiza validações básicas das requisições e trata erros comuns.

* **Service (`UserService`):** Contém a lógica de negócios da aplicação, incluindo o processamento do arquivo JSON, a filtragem de usuários, a agregação de dados por país e time, e a contagem de logins por data.

* **Model (`Model`):** Contém as classes de modelo que representam os dados da aplicação, como `User`, `LogUser`, e as classes de resposta personalizadas (`UploadResponse`, `ApiResponse`, `CountryTotal`, `TeamInsights`, `LoginCount`).

## Como Utilizar a API

1.  **Enviar o arquivo JSON de usuários:** Utilize uma ferramenta como Insomnia, Postman para enviar um arquivo JSON contendo a lista de usuários para o endpoint `POST /users` com o parâmetro `arquivo`. O `Content-Type` da requisição deve ser `multipart/form-data`.

2.  **Consultar os dados:** Após o upload bem-sucedido, você pode acessar os outros endpoints via requisições `GET`:
    * `/superusers`
    * `/top-countries`
    * `/team-insights`
    * `/active-users-per-day` (opcionalmente com o parâmetro `?min=3000`)

## Considerações e Melhorias Futuras

* **Persistência de Dados:** Atualmente, os dados dos usuários são armazenados em memória. Para uma aplicação mais robusta, seria ideal implementar a persistência em um banco de dados.
* **Tratamento de Erros Mais Detalhado:** Melhorar o tratamento de erros com códigos de status HTTP mais específicos e mensagens de erro mais informativas.
* **Validação de Dados:** Adicionar validação dos dados de entrada (tanto no upload quanto nos parâmetros de consulta) para garantir a integridade dos dados processados.
* **Testes Unitários e de Integração:** Implementar testes para garantir a funcionalidade e a qualidade da aplicação.
* **Paginação:** Para grandes volumes de dados, implementar paginação nas respostas das listagens seria crucial para a performance.
* **Documentação da API:** Utilizar ferramentas como Swagger (OpenAPI) para gerar uma documentação interativa da API.



---
# Desafio Técnico: Performance e Análise de Dados via API

## Objetivo

Você tem 1 hora para criar uma API que recebe um arquivo JSON com 100.000 usuários e oferece endpoints performáticos e bem estruturados para análise dos dados.

- [Exemplos de respostas esperadas na API](https://github.com/codecon-dev/desafio-1-1s-vs-3j/blob/main/exemplos-endpoints.json)
- [Arquivo com 100 mil usuários para importar](https://drive.google.com/file/d/1zOweCB2jidgHwirp_8oBnFyDgJKkWdDA/view?usp=sharing)
- [Arquivo com 1 mil usuário para teste](https://drive.google.com/file/d/1BX03cWxkvB_MbZN8_vtTJBDGiCufyO92/view?usp=sharing)

---

## JSON de entrada

O JSON contém uma lista de usuários com a seguinte estrutura:

```json
{
  "id": "uuid",
  "name": "string",
  "age": "int",
  "score": "int",
  "active": "bool",
  "country": "string",
  "team": {
    "name": "string",
    "leader": "bool",
    "projects": [{ "name": "string", "completed": "bool" }]
  },
  "logs": [{ "date": "YYYY-MM-DD", "action": "login/logout" }]
}
```

---

## Endpoints obrigatórios

### `POST /users`

Recebe e armazena os usuários na memória. Pode simular um banco de dados em memória.

### `GET /superusers`

- Filtro: `score >= 900` e `active = true`
- Retorna os dados e o tempo de processamento da requisição.

### `GET /top-countries`

- Agrupa os superusuários por país.
- Retorna os 5 países com maior número de superusuários.

### `GET /team-insights`

- Agrupa por `team.name`.
- Retorna: total de membros, líderes, projetos concluídos e % de membros ativos.

### `GET /active-users-per-day`

- Conta quantos logins aconteceram por data.
- Query param opcional: `?min=3000` para filtrar dias com pelo menos 3.000 logins.

### `GET /evaluation`

Ele deve executar uma autoavaliação dos principais endpoints da API e retornar um relatório de pontuação.

A avaliação deve testar:

- Se o status retornado é 200
- O tempo em milisegundos de resposta
- Se o retorno é um JSON válido

Esse endpoint pode rodar scripts de teste embutidos no próprio projeto e retornar um JSON com os resultados. Ele será utilizado para validar a entrega de forma automática e rápida.

---

## Requisitos Técnicos

- Tempo de resposta < 1s por endpoint.
- Todos os endpoints precisam retornar o tempo de processamento (em milissegundos) e a timestamp da requisição
- Código limpo, modular, com funções bem definidas.
- Pode usar qualquer linguagem/framework.
- Documentação ou explicação final vale pontos bônus.
- Não pode usar IA.
