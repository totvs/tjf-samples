# API Context

# Contexto

Para exemplificar a biblioteca __API Context__ vamos criar uma _API REST_ que possa trazer informações dos principais __Jedis__ do universo __Star Wars__. Esta _API_ precisa retornar as informações no formato `JSON` e deve seguir os padrões do [Guia de Implementação das APIs TOTVS][guia-api-totvs].

> É muito importante que, antes de iniciar o desenvolvimento deste _sample_, você tenha lido o [Guia de Implementação das APIs TOTVS][guia-api-totvs].


# Começando com TJF API Context

Para criação deste exemplo, vamos iniciar a explicação a partir de um projeto [Spring][spring] já criado, caso você não possua este projeto basta acessar o [Spring Initializr][spring-initializr] para criá-lo.


## Dependências

Além da dependência do [Spring][spring], para utilização do módulo __API Context__ é necessário inserir as seguintes dependências no arquivo `pom.xml` do projeto:

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
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- TJF -->
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-core</artifactId>
</dependency>
```


## Criando os dados

Para nosso exemplo utilizaremos um arquivo `JSON` para leitura das informações, portanto será necessário criar dentro da pasta `src/main/resources` o arquivo com o nome e conteúdo abaixo:

_Jedis.json_

```json
[
  {
    "name": "Qui-Gon Jinn",
    "gender": "male",
    "movies": [1]
  },
  {
    "name": "Obi-Wan Kenobi",
    "gender": "male",
    "movies": [1, 2, 3, 4, 5, 6]
  },
  {
    "name": "Anakin Skywalker",
    "gender": "male",
    "movies": [1, 2, 3]
  },
  {
    "name": "Yoda",
    "gender": "male",
    "movies": [1, 2, 3, 4, 5, 6]
  },
  {
    "name": "Mace Windu",
    "gender": "male",
    "movies": [1, 2, 3]
  },
  {
    "name": "Count Dooku",
    "gender": "male",
    "movies": [2, 3]
  },
  {
    "name": "Luke Skywalker",
    "gender": "male",
    "movies": [4, 5, 6, 7, 8]
  },
  {
    "name": "Rey",
    "gender": "female",
    "movies": [7, 8]
  }
]
```


## Modelos de dados

Antes de iniciar com a criação da _API REST_, precisaremos criar a classe com o modelo de dados que irá reprensentar a estrutura das informações utilizadas. Vamos criar o pacote `br.com.star.wars.model`, que irá agrupar os modelos de dados, contendo a classe que irá representar as informações de `Jedi`:

_Jedi.java_

```java
public class Jedi {
  private String name;
  private String gender;
  private List<Integer> movies;

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

  public List<Integer> getMovies() {
    return this.movies;
  }

  public void setMovies(List<Integer> movies) {
    this.movies = movies;
  }

}
```


# Criando a API REST

Para iniciar, vamos criar o pacote `br.com.star.wars.api` que irá agrupar as _APIs_. Lembrando que, como o TOTVS Java Framework utiliza o framework [Spring][spring], as classes de serviços _REST_ devem ser anotadas com [`@RestController`][@RestController].

Vamos desenvolver a _API_ responsável por retornar a lista dos principais Jedis (que serão obtidos através do arquivo `Jedis.json` que criamos anteriormente), criando a classe `JediController`, com o conteúdo abaixo:

_JediController.java_

```java
@RestController
@RequestMapping(path = "/api/v1/jedis", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class JediController {

  @GetMapping
  public List<Jedi> getAll() throws IOException {
    // Recupera o arquivos JSON com a lista de Jedis.
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    File file = new File(classLoader.getResource("Jedis.json").getFile());

    // Converte o JSON recuperado para List<Jedi>.
    ObjectMapper mapper = new ObjectMapper();
    List<Jedi> jedis = mapper.readValue(file,
        mapper.getTypeFactory().constructCollectionType(List.class, Jedi.class));

    return jedis;
  }

}
```

> Para indicar que nossa _API REST_ deve seguir os padrões do [Guia de Implementação das APIs TOTVS][guia-api-totvs] e interceptadas pelo __API Context__, a classe deve conter a anotação [`@ApiGuideline`][apiguideline] juntamente com a informação da versão do Guia.


## Padronizando o conteúdo de resposta

Ao analisar a classe `JediController`, podemos perceber que esta _API_ possui apenas um método `GET`, responsável por retornar todos os `Jedis`, acessível a partir da URL `/api/v1/jedis`. Porém, o conteúdo das informações que serão retornadas ainda não seguem o padrão do [Guia de Implementação das APIs TOTVS][guia-api-totvs] (com os atributos `hasNext` e `items`):

_http://localhost:8080/api/v1/jedis_

```http
GET /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

[
  {
    "name": "Qui-Gon Jinn",
    "gender": "male",
    "movies": [1]
  },
  ...
]
```

Vamos então alterar o méotodo acima para retornar e utilizar a classe [`ApiCollectionResponse`][apicollectionresponse], da biblioteca [__API Context__ (`com.totvs.tjf.tjf-api-context`)][tjf-api-context], que será responsável por padronizar a resposta do método `getAll()`.

_getAll()_

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll() throws IOException {
  // Recupera o arquivos JSON com a lista de Jedis.
  ClassLoader classLoader = ClassLoader.getSystemClassLoader();
  File file = new File(classLoader.getResource("Jedis.json").getFile());

  // Converte o JSON recuperado para List<Jedi>.
  ObjectMapper mapper = new ObjectMapper();
  List<Jedi> jedis = mapper.readValue(file,
      mapper.getTypeFactory().constructCollectionType(List.class, Jedi.class));

  return ApiCollectionResponse.of(jedis);
}
```

Com as alterações acima, ao executar nosso serviço _REST_, o retorno virá formatado nos padrões do [Guia de Implementação das APIs TOTVS][guia-api-totvs]:

_http://localhost:8080/api/v1/jedis_

```http
GET /api/v1/jedis HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "hasNext": false,
  "items": [
    {
      "name": "Qui-Gon Jinn",
      "gender": "male",
      "movies": [1]
    },
    ...
  ]
}
```

De acordo com o [Guia de Implementação das APIs TOTVS][guia-api-totvs], além da formatação do conteúdo das respostas é necessário que as _APIs REST_ possuam algumas funcionalidades padrões como ordenação e restrição de campos.


### Ordenação

Vamos então alterar nosso método para possibilitar a ordenação dos resultados. Para isso será necessário utilizar a classe [`ApiSortRequest`][apisortrequest], da biblioteca [__API Context__ (`com.totvs.tjf.tjf-api-context`)][tjf-api-context], responsável por nos retornar os atributos e a direção de ordenação informada na requisição:

_getAll()_

```java
@GetMapping
public ApiCollectionResponse<Jedi> getAll(ApiSortRequest sort) throws IOException {
  // Recupera o arquivos JSON com a lista de Jedis.
  ClassLoader classLoader = ClassLoader.getSystemClassLoader();
  File file = new File(classLoader.getResource("Jedis.json").getFile());

  // Converte o JSON recuperado para List<Jedi>.
  ObjectMapper mapper = new ObjectMapper();
  List<Jedi> jedis = mapper.readValue(file,
      mapper.getTypeFactory().constructCollectionType(List.class, Jedi.class));

  // Ordena a lista de Jedis.
  this.sortList(jedis, sort.getSort());

  return ApiCollectionResponse.of(jedis);
}
```

Vamos agora desenvolver o método `sortList` que será responsável por ordenar nossa lista de Jedis. Além deste, vamos desenvolver outros dois métodos; `sortListByName` e `sortListByGender` que serão responsáveis por ordenar a lista de Jedis por nome e por gênero, respectivamente.

_sortList()_, _sortListByName()_ e _sortListByGender()_

```java
private void sortList(List<Jedi> jedis, Map<String, ApiSortDirection> sort) {
  // Converte o Map de ordenação para List, assim poderemos navegar entre
  // a lista de valores de forma reversa para aplicar a ordenação na
  // lista de Jedis.
  List<Map.Entry<String, ApiSortDirection>> keys;
  keys = new ArrayList<Map.Entry<String, ApiSortDirection>>(sort.entrySet());

  for (int i = keys.size() - 1; i >= 0; i--) {
    ApiSortDirection direction = keys.get(i).getValue();

    switch (keys.get(i).getKey()) {
    case "name":
      this.sortListByName(jedis, direction);
      break;
    case "gender":
      this.sortListByGender(jedis, direction);
      break;
    }
  }
}

private void sortListByName(List<Jedi> jedis, ApiSortDirection direction) {
  Comparator<Jedi> compareByName = (Jedi j1, Jedi j2) -> j1.getName().compareTo(j2.getName());

  if (direction == ApiSortDirection.ASC) {
    jedis.sort(compareByName);
  } else {
    jedis.sort(compareByName.reversed());
  }
}

private void sortListByGender(List<Jedi> jedis, ApiSortDirection direction) {
  Comparator<Jedi> compareByGender = (Jedi j1, Jedi j2) -> j1.getGender().compareTo(j2.getGender());

  if (direction == ApiSortDirection.ASC) {
    jedis.sort(compareByGender);
  } else {
    jedis.sort(compareByGender.reversed());
  }
}
```

Adicionando os métodos acima, ao executar nosso serviço _REST_, o retorno virá ordenado pelos campos informados na requisição:

_http://localhost:8080/api/v1/jedis?order=name,-gender_

```http
GET /api/v1/jedis?order=name,-gender HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "hasNext": false,
  "items": [
    {
      "name": "Anakin Skywalker",
      ...
    },
    {
      "name": "Count Dooku",
      ...
    },
    {
      "name": "Luke Skywalker",
      ...
    },
    ...
  ]
}
```

# Isso é tudo pessoal!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos recursos proposto pelo componente __API Context__, mandar sugestões e melhorias para o projeto TJF.

> O conteúdo deste exemplo está em nosso repositório no [GitHub][github].

[tjf-api-context]: https://tjf.totvs.com.br/wiki/tjf-api-context
[guia-api-totvs]: http://tdn.totvs.com/x/nDUxE
[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[@RestController]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/RestController.html
[apiguideline]: https://tjf.totvs.com.br/wiki/tjf-api-context#estereotipos
[apicollectionresponse]: https://tjf.totvs.com.br/wiki/tjf-api-context#objetos-de-transferencia
[apisortrequest]: https://tjf.totvs.com.br/wiki/tjf-api-context#objetos-de-transferencia
[github]: https://github.com/totvs/tjf-api-context-sample