## AccountManager

## Propósito

Esta API REST é uma ideia básica de um sistema de back-office bancário  
voltado para gestão de contas e movimentações financeiras de clientes. 

## TECNOLOGIAS
É uma aplicação com um setup mínimo, contendo as seguints tecnologias:
* Java 17
* Spring Boot 3.4.5
* MongoDB
* Docker e Docker-compose
* Swagger/OpenAPI 2.8.8 para documentação [http://localhost:8080/api/v1/swagger-ui/index.html]
* Test Containers
* JUnit 5 / Mockito
## Endpoints
A aplicação expões os seguintes endpoints:
* Cadastrar novo cliente : POST (http://localhost:8080/api/v1/clients)
* Cadastrar nova conta bancária pra cliente existente: POST(http://localhost:8080/api/v1/accounts)
* Consultar saldo da conta cadastrada : GET (http://localhost:8080/api/v1/accounts/{id}/balance)
* Realizar movimentação entre contas: POST(http://localhost:8080/api/v1/transactions)
* Consultar extrato de movimentações : GET (http://localhost:8080/api/v1/accounts/{id}/transactions?startDate=...&endDate=...)

## Fazendo Build da Aplicação
O projeto usa maven como ferramenta para construir a aplicação. 
Para realizar o build deve instalar o maven localmente e executar os seguintes comandos:
* Para realizar o build enquanto roda os testes
```bash
  mvn clean package
```
* Para realizar o build e pular os testes
```bash
  mvn clean package -DskipTests
```

## Rodar a aplicação
O projeto usa o docker pra criar uma imagem do mongo e da aplicação. Logo o docker deve estar instalado.
Para rodar a aplicação execute os seguintes comandos:
* Criar imagem do docker
```bash
  docker compose build
```
* Rodar a aplicação
```bash
  docker compose up
```
ou 
```bash
  docker compose up -d
```
Isso vai inicializar a API na porta 8080

## Rodar os Testes
Para rodar os testes unitários e de Integração execute o seguinte comando:
```bash
  mvn test
```
## Documentação
Para documentação o endpoint do Swagger está exposto na seguinte url: http://localhost:8080/api/v1/swagger-ui/index.html

## Evidências
Abaixo estão evidências do funcionamento da API via Postman:
* Cadastrar cliente:
![image](https://github.com/user-attachments/assets/4077ba49-8631-46df-b7eb-92f94ecc0683)

* Cadastrar conta bancária:
  ![image](https://github.com/user-attachments/assets/2233e99b-c92d-4e4f-8b59-f269cd9af99e)
  
* Realizar movimentação entre contas:
![image](https://github.com/user-attachments/assets/5a036491-76a5-4037-b1b3-2dd3725c03ec)

* Consultar saldo da conta cadastrada
  ![image](https://github.com/user-attachments/assets/c8974b0b-63ef-4374-8a8e-7c69cc48a6d2)
* Consultar extrato de movimentações
  ![image](https://github.com/user-attachments/assets/bfb48066-e6ef-4c34-ac07-6f58f83974ef)
