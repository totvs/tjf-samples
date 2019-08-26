
# Exemplo de uso do componente Repository Aggregate

## Contexto

Para explicação do componente **TJF Repository Aggregate** vamos utilizar um exemplo simples com uma classe de Contas (account) de um banco.

## Começando com o TJF Repository Aggregate

Para criação deste exemplo, vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https:/start.spring.io) e criar o projeto.

Para fácil entendimento do componente **TJF Repository Aggregate** vamos seguir a sequencia a baixo para criação do exemplo.

### Dependências

Para utilização do componente será necessário inserir a seguinte dependência em seu arquivo pom.xml.

```
<dependency>
	<groupId>com.totvs.tjf</groupId>
	<artifactId>tjf-repository-aggregate</artifactId>
	<version>{{version}}</version> <!-- Utilize a versão desejada -->
</dependency>
```

Como iremos criar um micro serviço REST, para facilitar a criação do Model, vamos utilizar o Lombok como dependência.

```
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>1.18.8</version>
	<scope>provided</scope>
</dependency>
```

Mais informações sobre o Lombok: [https://projectlombok.org/](https://projectlombok.org/)

Como precisaremos de um banco PostgreSQL, também o teremos como dependência:

```
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<version>42.2.5</version>
</dependency>
```
Além das dependências do Spring Boot que já deverão vir com o Spring initializr:

```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<version>2.1.3.RELEASE</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
	<version>2.1.3.RELEASE</version>
</dependency>
```

### Criando o código fonte
Agora, com as dependências já definidas, podemos criar nossos códigos fontes. Para começar, vamos criar os pacotes do projeto, utilizando os seguintes nomes:
* com.tjf.sample.github.aggregate
* com.tjf.sample.github.validation.controller
* com.tjf.sample.github.validation.model
* com.tjf.sample.github.validation.repository

Que no final ficará assim:

![Estrutura dos pacotes Java](resources/estrutura_pacotes.png)

**Importante**: *Em nosso **SpringApplication** não iremos fazer alterações,* se houver interesse, o nome poderá ser alterado para AggregateApplication.

Bom, vamos lá! Começaremos pelo pacote Model, onde ficará nosso modelo do dominio de nossa aplicação. Vamos criar apenas uma classe.

*AccountModel.java*

```java
package com.tjf.sample.github.aggregate.model;

import java.io.Serializable;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import lombok.Getter;

@Getter
@Aggregate
public class AccountModel implements Serializable {

	private static final long serialVersionUID = 3181541174652364912L;

	@AggregateIdentifier
	private Integer accountId;

	private String name;

	private String address;

	private Double balance;

}
```
**Importante**: Não podemos esquecer de definir um campo com a *annotation @AggregateIdentifier*, conforme descrito na documentação do tjf-repository-aggregate.

Agora vamos criar o Controller da nossa API:

*AccountController.java*

```java
package com.tjf.sample.github.aggregate.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.aggregate.model.AccountModel;
import com.tjf.sample.github.aggregate.repository.AccountRepository;

@RestController
@RequestMapping(path = "/api/v1/sample", produces = { APPLICATION_JSON_VALUE })
public class AccountController {

	@Autowired
	private AccountRepository repository;

	@PostMapping("account")
	@Transactional
	public void postAccount(@RequestBody AccountModel account) {

		this.repository.insert(account);
	}
}
```

Por fim, vamos criar nosso repository:

*AccountRepository.java*

```java
package com.tjf.sample.github.aggregate.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.aggregate.model.AccountModel;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

@Repository
public class AccountRepository extends CrudAggregateRepository<AccountModel, String> {

    public AccountRepository(EntityManager em, ObjectMapper mapper) {
        super(em, mapper);
    }

    @Override
    protected String getTableName() {
        return "sample_account";
    }
}
```

### Vamos testar?

Bom, já terminamos nosso *sample* e agora podemos brincar um pouco com o que foi implementado. Para testar, precisaremos de um banco PostgreSQL devidamente configurado. Para isso, temos um arquivo *docker compose* (pasta docker no sample):

```
version: '3'

volumes:

  sample-pg-data: {}

services:

  sample-postgres:
    image: sameersbn/postgresql:10-1
    restart: always
    environment:
      DB_NAME: sample_account
      DB_USER: dev-sample
      DB_PASS: dev-sample
      PG_PASSWORD: totvs@123
    ports:
      - 5432:5432
    volumes:
      - sample-pg-data:/var/lib/postgresql

  sample-pg-admin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: dev-sample@totvs.com.br
      PGADMIN_DEFAULT_PASSWORD: dev-sample
    ports:
      - 8080:80
```

A seguir, precisamos configurar nosso Application.yml (src/main/resources):

```
spring:

  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/sample_account
    username: dev-sample
    password: dev-sample

  jpa:
    open-in-view: false
    database-platform: org.hibernate.dialect.PostgreSQL95Dialect
    hibernate:
      naming:
        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true # https://github.com/spring-projects/spring-boot/issues/12007

server:
  port: 8180
```


Agora vamos testar! Após subir a aplicação, vamos enviar um POST para o endpoint *http://localhost:8080/api/v1/sample/account* com o conteúdo abaixo no body.

```json
{
	"accountId":10,
	"name":"Sample",
	"address":"ProjetoTestes",
	"balance":400.0
}
```

Vamos conferir na URL *http://localhost:8080/browser/* e executar um *select* na nossa tabela *sample_account* e esse será o resultado:

![Registros do banco de dados](resources/registro_banco.png)


## Finalizando

Pronto! Agora já poderemos implementar as funcionalidades do módulo **TJF Repository Aggregate** nos nossos projetos. Lembrando que as informações técnicas se encontram no próprio README.md do módulo.
Este exemplo está em nosso repositório no [GitHub](https://github.com/totvs/tjf-repository-aggregate-sample).