# Exemplo de uso do componente Repository Aggregate

## Contexto

Para exemplificar o uso da biblioteca **Repository Aggregate**, criaremos APIs REST que possibilite a criação de uma árvore genealógica dos personagens do universo **Star Wars**.

Os registros de cada personagem e das árvores genealógicas serão armazenadas em banco de dados em entidades que possuem as informações dos registros agregados no formato `JSON` - removendo assim o _boilerplate_ de "de-para" entre a camada de domínio e a camada de infra.

## Começando

Iniciaremos o desenvolvimento criando um novo projeto Spring utilizando o serviço [Spring Initializr](https://start.spring.io). Precisamos adicionar como dependência os módulos **Spring Web Starter**, **PostgreSQL**, **Flyway** e o **Lombok**.

Após informados os dados acima e incluídas as dependências necessárias, podemos efetuar a geração do projeto.

## Configurações

Após gerado, precisamos substituir no arquivo `pom.xml` o _parent_ do projeto pela biblioteca **TJF Boot Starter**:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
  <relativePath />
</parent>
```

Incluiremos também a dependência para utilização da biblioteca **Repository Aggregate** e as configurações do repositório Maven com a distribuição do TJF:

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
    <id>central-release</id>
    <name>TOTVS Java Framework: Release</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release</url>
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
    url: jdbc:postgresql://localhost:5432/swfamilytree
    username: postgres
    password: postgres

  # Configurações JPA
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQL95Dialect
    properties:
      hibernate:
        jdbc:
          lob:
            non_contextual_creation: true
```

Nas configurações acima, definimos qual _driver_ será utilizado para conexão com o banco de dados, o nome do banco, usuário e senha de acesso.

Precisamos também do _script_ de criação da tabela no banco de dados. Este _script_ deve ficar na pasta `src/main/resources/db/migration` com o nome `V1.0__initialize.sql` para que seja feita a execução automática pelo Flyway:

**V1.0__initialize.sql**

```sql
CREATE TABLE person
(
   id VARCHAR (36) NOT NULL,
   data JSONB NOT NULL,
   metadata JSONB NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE familytree
(
   id VARCHAR (36) NOT NULL,
   data JSONB NOT NULL,
   metadata JSONB NOT NULL,
   PRIMARY KEY (id)
);
```

> As entidades possuem apenas três colunas, pois todas as informações de cada registro serão armazenadas na coluna `data` no formato `JSON` e na coluna `metadada` ficarão informações internas da tablea como e data e usuário que efetuou a última atualização na tabela.

### Modelo de dados

Agora precisamos criar as classes que representam cada uma das entidades do nosso banco de dados.

As classes destas entidades devem ser anotadas com `@Aggregate` e devem possuir os atributos que a coluna `data` possui (representados pelos atributos da classe).

#### Entidades

Para iniciar criaremos as classes de modelo de dados das tabelas `Person` e `FamilyTree` além das classes que irão nos auxiliar no desenvolvimento:

**Human.java**

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Human {

  private String name;
  private String gender;

}
```

**Person.java**

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
public class Person extends Human {

  @AggregateIdentifier
  private String id;

}
```

**Relative.java**

```java
@Getter
@Setter
@NoArgsConstructor
public class Relative extends Person {

  private String relationship;

  public Relative(String id, String name, String gender, String relationship) {
    setId(id);
    setName(name);
    setGender(gender);
    setRelationship(relationship);
  }

}
```

**FamilyTree.java**

```java
@Getter
@Setter
@Aggregate
public class FamilyTree {

  @AggregateIdentifier
  private String id;
  private Person person;
  private List<Relative> relatives = new ArrayList<>();

  public FamilyTree() {
    this.id = UUID.randomUUID().toString();
  }

  public FamilyTree(Person person) {
    this();
    this.person = person;
  }

  public FamilyTree(Person person, List<Relative> relatives) {
    this();
    this.person = person;
    this.relatives = relatives;
  }

  public void addRelative(Relative relative) {
    this.relatives.add(relative);
  }

}
```

#### Repositories

Após criadas as classes das entidades, criaremos os repositórios responsáveis pela criação e leitura dos registros das tabelas `person` e `familytree` no banco de dados:

**PersonRepository.java**

```java
@Repository
public class PersonRepository extends CrudAggregateRepository<Person, String> {

  public PersonRepository(EntityManager em, ObjectMapper mapper) {
    super(em, mapper);
  }

}
```

**FamilyTreeRepository.java**

```java
@Repository
public class FamilyTreeRepository extends CrudAggregateRepository<FamilyTree, String> {

  public FamilyTreeRepository(EntityManager em, ObjectMapper mapper) {
    super(em, mapper);
  }

}
```

### APIs REST

Vamos agora criar nossas APIs REST para manutenção das entidades `person` e `familytree` respectivamente:

**PersonController.java**

```java
@RestController
@RequestMapping(path = "api/v1/person",
  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

  @Autowired
  private PersonRepository repository;

  @PostMapping
  public void insert(@RequestBody Person person) {
    repository.insert(person);
  }

}
```

**FamilyTreeController.java**

```java
@RestController
@RequestMapping(path = "api/v1/familytree",
  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class FamilyTreeController {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private FamilyTreeRepository treeRepository;

  @PostMapping("person/{personId}/relative/{relativeId}/{relationship}")
  public void addPerson(@PathVariable String personId, @PathVariable String relativeId,
      @PathVariable String relationship) {

    FamilyTree fm = treeRepository
        .findOne("data->'person' @> ?", new SqlParameterValue(Types.OTHER, "{\"id\":\"" + personId + "\"}"))
        .orElse(null);

    if (fm == null) {
      Person person = personRepository.get(personId).orElseThrow();
      fm = new FamilyTree(person);
      treeRepository.insert(fm);
    }

    Person relativePerson = personRepository.get(relativeId).orElseThrow();
    Relative relative = new Relative(relativePerson.getId(), relativePerson.getName(), relativePerson.getGender(),
        relationship);
    fm.addRelative(relative);

    treeRepository.update(fm);
  }

  @GetMapping("person/{personId}")
  public FamilyTree getPersonFamilyTree(@PathVariable String personId) {
    return treeRepository
        .findOne("data->'person' @> ?", new SqlParameterValue(Types.OTHER, "{\"id\":\"" + personId + "\"}"))
        .orElse(null);
  }

}
```

### Iniciando o banco de dados

Antes de dar início a execução do projeto precisamos iniciar o serviço do banco de dados. Para isto vamos criar um arquivo `docker-compose.yml` para a criação do banco de dados:

**docker-compose.yml**

```yml
version: '3'

services:
  db:
    image: postgres
    restart: always
    environment:
      POSTGRES_PASSWORD: postgres
      POSTGRES_USER: postgres
      POSTGRES_DB: swfamilytree
    ports:
      - 5432:5432

  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: sample@test.com
      PGADMIN_DEFAULT_PASSWORD: postgres
    ports:
      - 5480:80
```

Para iniciar os serviços basta executar o comando abaixo na pasta onde foi criado o arquivo:

```command
docker-compose up -d
```

> O acesso a ferramenta de gerenciamento do banco de dados fica disponível em http://localhost:5480 com o e-mail e senha definidos acima.

### Criação dos registros

Após finalizado o desenvolvimento das APIs REST podemos executar nosso projeto, como um **Spring Boot App**, e iniciar a criação dos registros conforme a figura dos personagens abaixo:

![Star Wars Family Tree](resources/sw-familytree.png)

- **Anakin Skywalker** e **Padmé Amidala** são pais de **Luke Skywalker** e **Leia Organa**; e
- **Leia Organa** e **Han Solo** são pais de **Ben Solo**.

Vamos iniciar com a criação de cada um destes personagens. Para isto basta efetuar uma requisição _HTTP POST_ para cada um deles conforme as informações abaixo:

**Anakin Skywalker**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "1d069927-2c3d-4ebd-8678-0ca5d76bae9a",
  "name": "Anakin Skywalker",
  "gender": "male"
}
```

**Padmé Amidala**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "b7325afa-8302-4332-8f8a-ddaa063888e2",
  "name": "Padmé Amidala",
  "gender": "female"
}
```

**Luke Skywalker**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "82f0a882-a87c-4ad6-881b-8ee30cb3dbe9",
  "name": "Luke Skywalker",
  "gender": "male"
}
```

**Leia Organa**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "f64e3a46-7624-4764-991a-f12b536d841f",
  "name": "Leia Organa",
  "gender": "female"
}
```

**Han Solo**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "bf10bd9d-31fb-44bc-98f5-0e53615ab1bb",
  "name": "Han Solo",
  "gender": "male"
}
```

**Ben Solo**

```http
POST /api/v1/person HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "id": "320c7cb0-ef2e-4d00-9477-1bb50b16d725",
  "name": "Ben Solo",
  "gender": "male"
}
```

Agora que criamos os personagens, vamos criar o relacionamento entre eles. Como exemplo, vamos nos concentrar apenas na personagem **Padmé Amidala** e a cada requisição adicionar os personagens descendentes dela:

**Luke Skywalker: filho**

```http
POST /api/v1/familytree/person/b7325afa-8302-4332-8f8a-ddaa063888e2/relative/82f0a882-a87c-4ad6-881b-8ee30cb3dbe9/child HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

**Leia Organa: filha**

```http
POST /api/v1/familytree/person/b7325afa-8302-4332-8f8a-ddaa063888e2/relative/f64e3a46-7624-4764-991a-f12b536d841f/child HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

**Ben Solo: neto**

```http
POST /api/v1/familytree/person/b7325afa-8302-4332-8f8a-ddaa063888e2/relative/320c7cb0-ef2e-4d00-9477-1bb50b16d725/grandchild HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

Após realizadas as inclusões acima, podemos recuperar a ávore genealógica da personagem **Padmé Amidala**:

```http
GET /api/v1/familytree/person/b7325afa-8302-4332-8f8a-ddaa063888e2 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

**Resultado:**

```json
{
  "id": "2188cde7-da3f-4eea-954b-25a084f54be6",
  "person": {
    "name": "Padmé Amidala",
    "gender": "female",
    "id": "b7325afa-8302-4332-8f8a-ddaa063888e2"
  },
  "relatives": [
    {
      "name": "Luke Skywalker",
      "gender": "male",
      "id": "82f0a882-a87c-4ad6-881b-8ee30cb3dbe9",
      "relationship": "child"
    },
    {
      "name": "Leia Organa",
      "gender": "female",
      "id": "f64e3a46-7624-4764-991a-f12b536d841f",
      "relationship": "child"
    },
    {
      "name": "Ben Solo",
      "gender": "male",
      "id": "320c7cb0-ef2e-4d00-9477-1bb50b16d725",
      "relationship": "grandchild"
    }
  ]
}
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **Repository Aggregate**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-repository-aggregate) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
