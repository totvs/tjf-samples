# TJF Repository Aggregate

*Sample* de utilização da biblioteca **TJF Repository Aggregate** do **TOTVS Java Framework**.

## Contexto

Para exemplificar o uso da biblioteca [TJF Repository Aggregate](https://tjf.totvs.com.br/wiki/tjf-repository-aggregate), criaremos algumas *APIs REST* que possibilite a criação de uma árvore geneológica dos personagens do universo **Star Wars**.

Os registros de cada personagem e das árvores geneológicas serão armazenadas em banco de dados em entidades que possuem as informações dos registros agragados no formato `JSON` - removendo assim o *boilerplate* de "de-para" entre a camada de domínio e a camada de infra.

Vamos criar estas entidades de forma que o registro final de uma árvore geneológica fique com a seguinte estrutura:

```json
{
  "id": "412955e5-c530-44d1-89be-87f6c33f370a",
  "name": "Leia Organa",
  "gender": "female",
  "relatives": [{
    "id": "d433b940-5082-4c67-a59c-7cd8f9163d60",
    "name": "Ben Solo",
    "gender": "male",
    "relationship": "son"
  }]
}
```

> Como engine de banco de dados utilizaremos o [PostgreSQL](https://www.postgresql.org).

## Começando

Iniciaremos o desenvolvimento criando um novo projeto [Spring](https://spring.io) utilizando o serviço [Spring Initializr](https://start.spring.io). O projeto deve possuir as configurações conforme abaixo:

![Spring Initializr](resources/spring-initializr.png)

Precisamos adicionar como dependência os módulos **Spring Web Starter** e **PostgreSQL Driver**. Após informados os dados acima e incluídas as dependências necessárias, podemos efetuar a geração do projeto.

## Configurações

Após gerado, precisamos substituir no arquivo `pom.xml` o *parent* do projeto pela biblioteca [TJF Boot Starter](https://tjf.totvs.com.br/wiki/tjf-boot-starter):

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>1.4.0-RELEASE</version>
</parent>
```

Incluiremos também a dependência para utilização da biblioteca **Repository Aggregate** e as configurações do repositório **Maven** com a distribuição do **TOTVS Java Framework**:

**Dependências**

```xml
<dependencies>
  ...

  <!-- TJF -->
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-repository-aggregate</artifactId>
  </dependency>

</dependencies>
```

**Repositórios**

```xml
<repositories>

  <repository>
    <id>tjf-release</id>
    <name>TOTVS Java Framework: Releases</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release/</url>
  </repository>

</repositories>
```

Por fim, precisamos renomear o arquivo `application.properties`, da pasta `src/main/resources`, para `application.yml`.

### Banco de dados

As configurações do banco de dados devem ser incluídas no arquivo `application.yml`:

```yml
spring:

  # Configurações banco de dados
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/starwars-familytree
    username: postgres
    password: ********

  # Configurações JPA
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQL95Dialect

```

Nas configurações acima, definimos qual *driver* será utilizado para conexão com o banco de dados, o nome do banco (`starwars-familytree`), usuário (`postgres`) e senha de acesso.

Precisamos também do *script* de criação da tabela no banco de dados. Este *script* deve ficar na pasta `src/main/resources/db/migration` com o nome `V1.0__initialize.sql` para que seja feita a execução automática pelo [Flyway](https://flywaydb.org).

**V1.0__initialize.sql**

```sql
CREATE TABLE person (
	id VARCHAR(36) NOT NULL,
	data JSONB NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE familytree (
	id VARCHAR(36) NOT NULL,
	data JSONB NOT NULL,
	PRIMARY KEY(id)
);
```

> As entidades apenas duas colunas, pois todas as informações de cada registro serão armazenadas na coluna `data` no formato `JSON`.

### Utilitários

Antes de iniciar a criação das classes de modelos de dados, criaremos alguns utilitários que irão nos auxiliar no desenvolvimento.

Para isto, vamos criar o pacote `br.com.starwars.familytree.enums` e criar dentro deste pacote dois `enums`: um para representar o gênero de cada personagem e o outro para representar o relacionamento entre um personagem e outro.

**Gender.java**

```java
package br.com.starwars.familytree.enums;

public enum Gender {

	MALE("male"), FEMALE("female"), OTHER("other");

	private final String gender;

	private Gender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

}
```

**Relationship.java**

```java
package br.com.starwars.familytree.enums;

public enum Relationship {

	GRANDFATHER("grandfather"), GRANDMOTHER("grandmother"), FATHER("parent"), MOTHER("mother"), GODFATHER("godfather"),
	HUSBAND("husband"), WIFE("wife"), SON("son"), DAUGHTER("daughter"), GRANDSON("grandson"),
	GRANDDAUGHTER("granddaughter");

	private final String relationship;

	private Relationship(String relationship) {
		this.relationship = relationship;
	}

	public String getRelationship() {
		return relationship;
	}

}
```

### Modelos de dados

Agora precisamos criar as classes que representam cada uma das entidades do nosso banco de dados.

As classes destas entidades devem ser anotadas com `@Aggregate` e devem possuir os atributos que a coluna `data` possui (representados pelos atributos da classe).

#### Entidades

Para iniciar, criaremos o pacote `br.com.starwars.familytree.model` e dentro deste pacote criaremos as classes de modelo de dados das tabelas `person` e `familytree` além de outras classes que irão nos auxiliar no desenvolvimento.

**Human.java**

```java
package br.com.starwars.familytree.model;

import br.com.starwars.familytree.enums.Gender;

public abstract class Human {

	private final String name;
	private final Gender gender;

	public Human(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

}
```

**Person.java**

```java
package br.com.starwars.familytree.model;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import br.com.starwars.familytree.enums.Gender;

@Aggregate
public class Person extends Human {

	@AggregateIdentifier
	private final String id;

	public Person(String id, String name, Gender gender) {
		super(name, gender);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
```

**Relative.java**

```java
package br.com.starwars.familytree.model;

import br.com.starwars.familytree.enums.Gender;
import br.com.starwars.familytree.enums.Relationship;

public class Relative extends Person {

	private final Relationship relationship;

	public Relative(String id, String name, Gender gender, Relationship relationship) {
		super(id, name, gender);
		this.relationship = relationship;
	}

	public Relationship getRelationship() {
		return relationship;
	}

}
```

**FamilyTree.java**

```java
package br.com.starwars.familytree.model;

import java.util.ArrayList;
import java.util.List;

import com.totvs.tjf.core.stereotype.Aggregate;

import br.com.starwars.familytree.enums.Gender;

@Aggregate
public class FamilyTree extends Person {

	private List<Relative> relatives = new ArrayList<>();

	public FamilyTree(String id, String name, Gender gender) {
		super(id, name, gender);
	}

	public FamilyTree(String id, String name, Gender gender, List<Relative> relatives) {
		super(id, name, gender);
		this.relatives = relatives;
	}

	public void addRelative(Relative relative) {
		this.relatives.add(relative);
	}

}
```

#### Repositories

Após criadas as classes das entidades, criaremos os **repository** - responsáveis pela criação e leitura dos registros das tabelas `person` e `familytree` no banco de dados - dentro do pacote `br.com.starwars.familytree.repository`.

**PersonRepository.java**

```java
package br.com.starwars.familytree.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

import br.com.starwars.familytree.model.Person;

@Repository
public class PersonRepository extends CrudAggregateRepository<Person, String> {

	public PersonRepository(EntityManager em, ObjectMapper mapper) {
		super(em, mapper);
	}

}
```

**FamilyTreeRepository.java**

```java
package br.com.starwars.familytree.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

import br.com.starwars.familytree.model.FamilyTree;

@Repository
public class FamilyTreeRepository extends CrudAggregateRepository<FamilyTree, String> {

	public FamilyTreeRepository(EntityManager em, ObjectMapper mapper) {
		super(em, mapper);
	}

}
```