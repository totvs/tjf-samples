# API Context

Para exemplificar a biblioteca [TJF API Context][tjf-api-context] vamos criar uma _API REST_ que possa trazer informações dos principais **Jedis** do universo **Star Wars**.

As informações devem ser retornadas no formato `JSON` e seguir os padrões do [guia de implementação das APIs TOTVS][guia-api-totvs].

## Começando

Para este exemplo vamos criar uma aplicação [Spring][spring] gerada a partir do [Spring Initializr][spring-initializr]. Na geração da aplicação adicione como dependência as bibliotecas **Spring WEB** e **Spring Data JPA**.

### Dependências

Além das dependências do [Spring][spring] mencionadas acima, para utilização do [TJF API Context][tjf-api-context] é necessário alterar o `parent` da aplicação no arquivo `pom.xml`:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>1.23.0-RELEASE</version>
</parent>
```

E incluir as depêndencias do [TJF][tjf] abaixo:

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

> Como o [TJF API Context][tjf-api-context] fornece apenas implementações contratuais, as execuções destas implementações ficam nas bibliotecas [TJF API Core][tjf-api-core] e [TJF API JPA][tjf-api-jpa].

Vamos incluir também a dependência do banco de dados que será utilizado pela aplicação, que neste caso será o [H2][h2]:

```xml
<!-- Database -->
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```

### Configuração

Para que a aplicação possa comunicar-se com o banco de dados, precisamos alterar sua configuração no arquivo `application.yaml` dentro da pasta `src/main/resources`.

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

Primeiramente precisamos habilitar nossa aplicação para utilizar o repositório [`ApiJpaRepository`][apijparepository] que facilita as consultas respeitando o [guia de implementação das APIs TOTVS][guia-api-totvs]. Vamos então incluir a anotação [`@EnableJpaRepositories`][enablejparepositories] e defini-lo como `"repositoryBaseClass"` na classe principal da aplicação:

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class SWApiContextApplication {

  public static void main(String[] args) {
    SpringApplication.run(SWApiContextApplication.class, args);
  }

}
```

Depois podemos iniciar a criação da classe da entidade **Jedi**:

**`Jedi.java`**

```java
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

	// Métodos Getters e Setters

}
```

E criaremos também a interface de repositório desta entidade:

**`JediRepository.java`**

```java
@Repository
@Transactional
public interface JediRepository extends JpaRepository<Jedi, Integer>, ApiJpaRepository<Jedi> {}
```

## API REST

Com as classe da entidade e respositório criadas, iniciaremos o desenvolvimento da _API REST_ responsável pela consulta e manutenção de Jedis. É importante que a nossa _API_ seja anotada com [`@ApiGuideline`][apiguideline] para que as respostas adequem-se ao [guia de implementação das APIs TOTVS][guia-api-totvs].

### Adicionando um novo Jedi

Primeiramente vamos desenvolver o método responsável por criar um novo Jedi, a partir de uma requisição `POST`:

**`JediController.java`**

```java
@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
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

E a resposta da requisição deve ser semlhante ao conteúdo abaixo:

```json
{
  "id": 1,
  "name": "Luke Skywalker",
  "gender": "male"
}
```

#### Tratamento de erros

Após criado o método que permite recuperar o Jedi pelo seu respectivo código, será necessário tratar possíveis inconsistências que podem ocorrer se o usuário informar um código de Jedi inexistente, por exemplo.

Conforme o [guia de implementação das APIs TOTVS][guia-api-totvs], o correto neste caso é que a resposta seja uma mensagem de erro tratada e status HTTP `404 Not Found`. Para isto vamos criar uma `Exception` específica e nesta utilizar a anotação [`@ApiError`][apierror]:

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

> É possível também utilizar a anotação [`@ApiNotFound`][apinotfound] sem que seja necessário informar o status HTTP.

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

Já criamos o método que retorna um determinado Jedi pelo seu código, vamos então criar um método que nos retorne a lista de Jedis. Para este método será utilizada a classe [`ApiCollectionResponse`][apicollectionresponse] que padroniza a resposta conforme definido pelo [guia de implementação das APIs TOTVS][guia-api-totvs]:

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll() {
  return ApiCollectionResponse.of(jediRepo.findAll());
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

Para facilitar a execução das funcionalidades acima durante a pesquisa dos registros, vamos alterar o método `getAll` da nossa _API_ e incluir os objetos das classes [`ApiFieldRequest`][apifieldrequest], [`ApiPageRequest`][apipagerequest] e [`ApiSortRequest`][apisortrequest], respectivamente responsáveis por recuperar os parâmetros de filtro de conteúdo de resposta, paginação e ordenação da requisição.

Também vamos utilizar o método `findAllProjected` do repositório, que facilita a consulta de registros utilizando os objetos acima:

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll(ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
  return jediRepo.findAllProjected(field, page, sort);
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

# Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos recursos proposto pelo componente **API Context**, mandar sugestões e melhorias para o projeto TJF.

[tjf]: https://tjf.totvs.com.br
[tjf-api-context]: https://tjf.totvs.com.br/wiki/tjf-api-context
[tjf-api-core]: https://tjf.totvs.com.br/wiki/tjf-api-core
[tjf-api-jpa]: https://tjf.totvs.com.br/wiki/tjf-api-jpa
[apiguideline]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/stereotype/ApiGuideline.html
[apicollectionresponse]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/v1/response/ApiCollectionResponse.html
[apisortrequest]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/v1/request/ApiSortRequest.html
[apipagerequest]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/v1/request/ApiPageRequest.html
[apifieldrequest]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/v1/request/ApiFieldRequest.html
[apijparepository]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/jpa/repository/ApiJpaRepository.html
[apierror]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/stereotype/ApiError.html
[apinotfound]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/stereotype/error/ApiNotFound.html
[apicollectionresponse]: https://tjf.totvs.com.br/javadoc/com/totvs/tjf/api/context/v1/response/ApiCollectionResponse.html
[github]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-context-sample
[guia-api-totvs]: http://tdn.totvs.com/x/nDUxE
[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[restcontroller]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html
[enablejparepositories]: https://docs.spring.io/spring-data/data-jpa/docs/current/api/org/springframework/data/jpa/repository/config/EnableJpaRepositories.html
[h2]: https://www.h2database.com
