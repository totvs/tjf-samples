# API JPA

# Contexto

Para exemplificar a biblioteca __API JPA__ vamos utilizar o mesmo projeto criado para o _sample_ da biblioteca __API Context__ disponível em nosso [GitHub][tjf-api-context-sample].

> É muito importante que, antes de iniciar o desenvolvimento deste _sample_, você tenha lido e compreendido o _sample_ do __API Context__.


# Começando com API JPA

Com o projeto _sample_ do __API Context__ em mãos, vamos alterar a origem dos dados de consulta, atualmente disponíveis no arquivo `Jedis.json`, para o banco de dados. Com isso será necessário criar nossas classes de entidades e repositório.

A biblioteca __API JPA__ irá auxiliar na criação deste repositório e será responsável por incluir métodos padrões para a busca de dados já formatados e alterados para os padrões do [Guia de implementação das APIs TOTVS][guia-api-totvs].

> Como _engine_ de banco de dados utilizaremos o [H2][h2].


## Dependências

Além das já existentes, precisamos adicionar as seguintes dependências no arquivo `pom.xml` do projeto:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>1.11.0-RELEASE</version>
</parent>
```

```xml
<!-- Spring -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- TJF -->
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-jpa</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <version>1.4.199</version>
</dependency>
```

Teremos também que incluir as configurações do banco de dados no arquivo `application.yml`:

```yml
spring:
  h2:
    console:
      enabled: true
      path: /h2

  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb
    username: sa
    password:

  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
        ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
```

Observando as configurações acima, foi definido a partir da propriedade `spring.jpa.hibernate.dll-auto=update` que a criação e atualização das tabelas do banco de dados será realizada de forma automática pelo próprio [Hibernate][hibernate].


## Criando as tabelas

Para que seja possível a criação automática das tabelas pelo [Hibernate][hibernate], será necessário alterar nossa classe de modelo de dados `Jedi.java` incluindo as anotações necessárias para as definições de entidade:

_Jedi.java_

```java
@Entity
public class Jedi {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotNull
  private String name;

  @NotNull
  private String gender;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "jedi_movie", inverseJoinColumns = @JoinColumn(name = "movie_id"))
  private List<Movie> movies = new ArrayList<Movie>();

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public List<Movie> getMovies() {
    return this.movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

}
```

Observe que, além de incluídas as anotações comuns `@Entity`, `@Id` e `@NotNull`, a lista de filmes foi alterada de `Integer` para `Movie` e foi incluída a anotação `@ManyToMany` para esta lista. Isto implica na criação de uma segunda entidade chamada _Movie_ e na criação de outra entidade de relacionamento de _Jedi x Movies_. Para que seja possível a criação automática destas duas entidades, temos que desenvolver uma outra classe de modelo de dados que irá conter as informações da entidade `Movie`:

_Movie.java_

```java
@Entity
public class Movie {
  @Id
  private int id;

  @NotNull
  private String name;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
```

> Para a entidade de relacionamento `Jedi x Movies` não é necessário criar outra classe, já que o [Hibernate][hibernate] a cria conforme informações obtidas das entidades `Jedi` e `Movie`.


## Criando os dados

Para nosso exemplo utilizaremos um banco de dados que será criado automaticamente, bem como suas tabelas, ao iniciar a execução do projeto, porém precisamos dos dados para utilizar em nossas requisições. Vamos criar então o arquivo `data.sql` (que irá substituir o arquivo `Jedis.json`) na pasta `src/main/resources` com os _scripts_ de criação dos dados conforme abaixo:

_data.sql_

```sql
INSERT INTO jedi (id, name, gender) VALUES (1, 'Qui-Gon Jinn', 'male');
INSERT INTO jedi (id, name, gender) VALUES (2, 'Obi-Wan Kenobi', 'male');
INSERT INTO jedi (id, name, gender) VALUES (3, 'Anakin Skywalker', 'male');
INSERT INTO jedi (id, name, gender) VALUES (4, 'Yoda', 'male');
INSERT INTO jedi (id, name, gender) VALUES (5, 'Mace Windu', 'male');
INSERT INTO jedi (id, name, gender) VALUES (6, 'Count Dooku', 'male');
INSERT INTO jedi (id, name, gender) VALUES (7, 'Luke Skywalker', 'male');
INSERT INTO jedi (id, name, gender) VALUES (8, 'Rey', 'female');

INSERT INTO movie (id, name) VALUES (1, 'The Phantom Menace');
INSERT INTO movie (id, name) VALUES (2, 'Attack of the Clones');
INSERT INTO movie (id, name) VALUES (3, 'Revenge of the Sith');
INSERT INTO movie (id, name) VALUES (4, 'A New Hope');
INSERT INTO movie (id, name) VALUES (5, 'The Empire Strikes Back');
INSERT INTO movie (id, name) VALUES (6, 'The Return of the Jedi');
INSERT INTO movie (id, name) VALUES (7, 'The Force Awakens');
INSERT INTO movie (id, name) VALUES (8, 'The Last Jedi');

INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (1, 1);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 1);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 2);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 3);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 4);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 5);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (2, 6);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (3, 1);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (3, 2);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (3, 3);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 1);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 2);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 3);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 4);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 5);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (4, 6);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (5, 1);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (5, 2);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (5, 3);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (6, 2);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (6, 3);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (7, 4);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (7, 5);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (7, 6);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (7, 7);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (7, 8);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (8, 7);
INSERT INTO jedi_movie (jedi_id, movie_id) VALUES (8, 8);
```

Este arquivo também é executado automaticamente pelo [Spring][spring].

> Para acessar o banco e consultar os dados, após iniciado o projeto, acessar a URL http://localhost:8080/h2 com as mesmas informações do arquivo `application.yml`:
>
> ![H2 Database](resources/h2.png)

## Modelos de dados

Como agora estamos trabalhando com banco de dados, precisamos criar um repositório contendo os métodos de busca das informações e é aqui que vamos utilizar a biblioteca __API JPA__. Para iniciar, vamos incluir na classe principal do nosso projeto, `StarWarsServicesApplication`, a anotação [`@EnableJpaRepositories`][@EnableJpaRepositories] direcionando o `repositoryBaseClass` para a classe `ApiJpaRepositoryImpl`:

_StarWarsServicesApplication.java_

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class StarWarsServicesApplication {

  public static void main(String[] args) {
    SpringApplication.run(StarWarsServicesApplication.class, args);
  }

}
```

Após alterar a classe principal conforme o código acima, vamos criar a nossa interface de repositório dentro do pacote `br.com.star.wars.model` com o nome `JediRepository` que, além de implementar a classe `JpaRepository` padrão, também irá implementar a classe `ApiJpaRepository`, disponibilizando assim os métodos de busca que permitem a formatação dos dados conforme o [Guia de implementação das APIs TOTVS][guia-api-totvs]:

_JediRepository.java_

```java
@Repository
@Transactional
public interface JediRepository extends JpaRepository<Jedi, Integer>, ApiJpaRepository<Jedi> {}
```

# Alterando a API REST

Para finalizar, vamos alterar a nossa _API REST_ `JediController` utilizando o repositório recém criado para buscar os dados do banco de dados:

_JediController.java_

```java
@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
public class JediController {

  @Autowired
  private JediRepository jediRepository;

  @GetMapping
  public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
    return jediRepository.findAllProjected(field, page, sort);
  }

}
```

Podemos observar acima que os métodos para ordenação foram removidos, pois o método `findAllProjected`, além de formatar os dados, já os traz ordenados e paginados:

```http
GET /api/v1/jedis?page=1&pageSize=3&order=-name HTTP/1.1
Host: localhost:8080

{
  "hasNext": true,
  "items": [
    {
      "name": "Yoda",
      "gender": "male",
      "movies": [...]
    },
    {
      "name": "Rey",
      "gender": "female",
      "movies": [...]
    },
    {
      "name": "Qui-Gon Jinn",
      "gender": "male",
      "movies": [...]
    }
  ]
}
```

Muito mais simples e fácil, não?


# Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente __API JPA__ e enviar sugestões e melhorias para o projeto __TOTVS Java Framework__.


[tjf-api-context-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-context-sample
[guia-api-totvs]: http://tdn.totvs.com/x/nDUxE
[h2]: https://www.h2database.com
[hibernate]: https://hibernate.org
[spring]: https://spring.io
[@EnableJpaRepositories]: https://docs.spring.io/spring-data/data-jpa/docs/current/api/org/springframework/data/jpa/repository/config/EnableJpaRepositories.html
[tjf-api-context]: https://tjf.totvs.com.br/wiki/tjf-api-context
[github]: https://github.com/totvs/tjf-api-jpa-sample