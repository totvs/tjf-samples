# Exemplo de uso do componente API Context

## Contexto

Para exemplificar o uso da biblioteca **API Context** vamos criar uma API REST que possa trazer informações dos principais **Jedis** do universo **Star Wars**.

As informações devem ser retornadas no formato `JSON` e seguir os padrões do [guia de implementação das APIs TOTVS][guia-api-totvs].

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Na geração da aplicação adicione como dependência as bibliotecas **Spring WEB**, **Spring Data JPA**, **H2** e o **Lombok**.

### Dependências

Além das dependências do Spring mencionadas acima, para utilização do componente é necessário alterar o `parent` da aplicação no arquivo `pom.xml`:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
</parent>
```

E incluir as dependências abaixo:

```xml
<!-- TJF -->
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-core</artifactId>
</dependency>

<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-jpa</artifactId>
</dependency>
```

> Como o **API Context** fornece apenas implementações contratuais, as execuções destas implementações ficam nas bibliotecas **API Core** e **API JPA**.

Vamos adicionar também o repositório Maven de _release_ do TJF:

```xml
<repositories>
  <repository>
    <id>central-release</id>
    <name>TOTVS Java Framework: Releases</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release/</url>
  </repository>
</repositories>
```

### Configuração

Para que a aplicação possa comunicar-se com o banco de dados, precisamos alterar sua configuração no arquivo `application.yaml` da pasta `src/main/resources`.

**`application.yaml`**

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb

  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
```

## Modelo de dados

Primeiramente precisamos habilitar nossa aplicação para utilizar o repositório `ApiJpaRepository` que facilita as consultas respeitando o [guia de implementação das APIs TOTVS][guia-api-totvs].

Vamos então incluir a anotação `@EnableJpaRepositories` e defini-lo como `"repositoryBaseClass"` na classe principal da aplicação:

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class ApiContextApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiContextApplication.class, args);
  }

}
```

Depois podemos iniciar a criação da classe da entidade **Jedi**:

**`Jedi.java`**

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "jedi")
public class Jedi {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotNull
  private String name;

  @NotNull
  private String gender;

}
```

E criaremos também a interface de repositório desta entidade:

**`JediRepository.java`**

```java
@Repository
public interface JediRepository extends JpaRepository<Jedi, Integer>, ApiJpaRepository<Jedi> {}
```

## API REST

Com as classe da entidade e repositório criadas, iniciaremos o desenvolvimento da API REST responsável pela consulta e manutenção de Jedis. É importante que a nossa classe seja anotada com `@ApiGuideline` para que as respostas adequem-se ao [guia de implementação das APIs TOTVS][guia-api-totvs].

### Adicionando um novo Jedi

Primeiramente vamos desenvolver o método responsável por criar um novo Jedi, a partir de uma requisição `POST`:

**`JediController.java`**

```java
@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class JediController {

  @Autowired
  private JediRepository jediRepo;

  @PostMapping
  public Jedi add(@RequestBody Jedi jedi) {
    return jediRepo.saveAndFlush(jedi);
  }

}
```

Para testar vamos efetuar uma requisição com o corpo abaixo:

```http
POST /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "Luke Skywalker",
  "gender": "male"
}
```

E a resposta da requisição deve ser semelhante ao conteúdo abaixo:

```json
{
  "id": 1,
  "name": "Luke Skywalker",
  "gender": "male"
}
```

### Consultando um Jedi

Vamos criar agora o método que permite recuperar um Jedi pelo seu código:

```java
@GetMapping("/{id}")
public Jedi getOne(@PathVariable(required = true) int id) {
  return jediRepo.findById(id).get();
}
```

Para testar vamos efetuar uma requisição conforme abaixo:

```http
GET /api/v1/jedis/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

E a resposta da requisição deve ser semelhante ao conteúdo abaixo:

```json
{
  "id": 1,
  "name": "Luke Skywalker",
  "gender": "male"
}
```

#### Tratamento de erros

Após criado o método que permite recuperar o Jedi pelo seu respectivo código, será necessário tratar possíveis inconsistências que podem ocorrer se o usuário informar um código de Jedi inexistente, por exemplo.

Conforme o [guia de implementação das APIs TOTVS][guia-api-totvs], o correto neste caso é que a resposta seja uma mensagem de erro tratada e status HTTP `404 Not Found`. Para isto vamos criar uma `Exception` específica e nesta utilizar a anotação `@ApiError`:

**`JediNotFoundException.java`**

```java
@ApiError(status = HttpStatus.NOT_FOUND, value = "JediNotFoundException")
public class JediNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -52548244535504794L;

  @ApiErrorParameter
  private final int jediId;

  public JediNotFoundException(int jediId) {
    this.jediId = jediId;
  }

  public int getJediId() {
    return jediId;
  }

}
```

> É possível também utilizar a anotação `@ApiNotFound` sem que seja necessário informar o status HTTP.

Vamos precisar criar também o arquivo `messages.properties` na pasta `src/main/resources/i18n/exception` contendo a mensagem de erro que será utilizada na resposta de erro:

```properties
JediNotFoundException.message = Jedi não encontrado
JediNotFoundException.detail = Jedi de código {0} não foi encontrado
```

Agora podemos alterar nossa _API REST_ para lançar a exceção `JediNotFoundException` caso informado um código de Jedi inexistente:

```java
@GetMapping("/{id}")
public Jedi getOne(@PathVariable(required = true) int id) {
  return jediRepo.findById(id).orElseThrow(() -> {
    throw new JediNotFoundException(id);
  });
}
```

Se executarmos uma requisição com um código de Jedi inexistente conforme abaixo:

```http
GET /api/v1/jedis/99 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

A resposta da requisição terá a mensagem de erro com o status `404 Not Found`:

```json
{
  "code": "JediNotFoundException",
  "message": "Jedi não encontrado",
  "detailedMessage": "Jedi de código 99 não foi encontrado"
}
```

#### Filtro de Conteúdo de Resposta

Conforme definição do [guia de implementação das APIs TOTVS][guia-api-totvs], todo _endpoint_ deve permitir filtrar o conteúdo `JSON` de retorno através do parâmetro de URL `fields`. Para habilitar esta funcionalidade, precisamos incluir uma nova propriedade no arquivo de configuração da aplicação `application.yaml`:

```yml
tjf:
  api:
    filter:
      fields:
        enabled: true
```

Se executarmos a requisição abaixo:

```http
GET /api/v1/jedis/1?fields=name HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

A resposta trará apenas os atributos informados:

```json
{
  "name": "Luke Skywalker"
}
```

### Listando Jedis

Já criamos o método que retorna um determinado Jedi pelo seu código, vamos então criar um método que nos retorne a lista de Jedis. Para este método será utilizada a classe `ApiCollectionResponse` que padroniza a resposta conforme definido pelo [guia de implementação das APIs TOTVS][guia-api-totvs]:

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll() {
  return ApiCollectionResponse.from(jediRepo.findAll());
}
```

Antes de executarmos o método acima, vamos criar mais alguns Jedis:

```http
POST /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "Obi-Wan Kenobi",
  "gender": "male"
}
```

```http
POST /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "Yoda",
  "gender": "male"
}
```

```http
POST /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "Rey",
  "gender": "female"
}
```

Após criarmos os registros acima, vamos executar a requisição abaixo:

```http
GET /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

O retorno será a lista de todos os Jedis criados:

```json
{
  "hasNext": false,
  "items": [
    {
      "id": 1,
      "name": "Luke Skywalker",
      "gender": "male"
    },
    {
      "id": 2,
      "name": "Obi-Wan Kenobi",
      "gender": "male"
    },
    {
      "id": 3,
      "name": "Yoda",
      "gender": "male"
    },
    {
      "id": 4,
      "name": "Rey",
      "gender": "female"
    }
  ]
}
```

#### Paginação, Ordenação e Filtro de Conteúdo

Conforme o [guia de implementação das APIs TOTVS][guia-api-totvs] todos os _endpoints_ que retornam uma lista de registros devem suportar:

- paginação através dos parâmetros de URL `page` (representando a página solicitada) e `pageSize` (representando a quantidade de registros por página). Se não informados, os parâmetros terão valor 1 para `page` e 25 para `pageSize`. Exemplo: `/jedis?page=2&pageSize=3`;

- ordenação de conteúdo através do parâmetro `order`, de forma ascendente com o sinal de `+` e descendente com o sinal de `-` a frente do atributo que será ordenado (por padrão, se não informado o sinal, será considerado sempre ordenação ascendente). Exemplo: `/jedis?order=-name`;

- filtro de conteúdo de resposta através do parâmetro de URL `fields`. Exemplo: `/jedis?fields=name`.

Para facilitar a execução das funcionalidades acima durante a pesquisa dos registros, vamos alterar o método `getAll` da nossa _API_ e incluir os objetos das classes `ApiFieldRequest`, `ApiPageRequest` e `ApiSortRequest`, respectivamente responsáveis por recuperar os parâmetros de filtro de conteúdo de resposta, paginação e ordenação da requisição.

Também vamos utilizar o método `findAllProjected` do repositório, que facilita a consulta de registros utilizando os objetos acima:

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
  return ApiCollectionResponse.from(jediRepo.findAllProjected(field, page, sort));
}
```

Para testar vamos efetuar uma requisição definindo uma paginação, ordenação e filtrando o conteúdo:

```http
GET /api/v1/jedis?page=2&pageSize=2&order=-name&fields=name HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

Resposta:

```json
{
  "hasNext": true,
  "items": [
    {
      "name": "Rey"
    },
    {
      "name": "Obi-Wan Kenobi"
    }
  ]
}
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **API Context**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-api-context) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).

[guia-api-totvs]: http://tdn.totvs.com/x/nDUxE
