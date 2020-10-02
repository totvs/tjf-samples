# Exemplo de uso do componente API JPA (Specification)

Exemplo para utilização de _specification_ com o módulo **API JPA**.

## Contexto

Para exemplificar o uso da técnica _specification_, criaremos um exemplo para busca de Droids, filtrando estes por suas características.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Na geração da aplicação adicione como dependência as bibliotecas **Spring Starter Web**, **H2** e o **Lombok**.

## Configurações

Além das dependências do Spring mencionadas acima, para utilização do componente é necessário alterar o `parent` da aplicação no arquivo `pom.xml`:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
  <relativePath />
</parent>
```

E incluir as dependências abaixo:

```xml
<!-- TJF -->
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-jpa</artifactId>
</dependency>
```

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

Antes de iniciarmos a codificação, vamos configurar a conexão com o banco de dados H2, para isso no seu arquivo `application.yml` insira as propriedades

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb
    username: sa

  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
```

## Codificando

Feitas as configurações, vamos criar um script para criação dos nossos Droids, assim quando iniciarmos a aplicação nosso banco de dados será preenchido de forma automática.

Para isso na pasta `src/main/resources` crie o arquivo _data.sql_ com o seguinte script:

```sql
INSERT INTO droid (name, function, height) VALUES ('Super Battle Droid', 'Super Soldado de Batalha', 1.91);
INSERT INTO droid (name, function, height) VALUES ('Protocol Droid', 'Auxiliam diplomantas e políticos; Programados em etiqueta e equipados com formidáveis ​​habilidades linguísticas', 1.70);
INSERT INTO droid (name, function, height) VALUES ('Sith Probe Droid', 'Rastrear fugitivos', 0.3);
INSERT INTO droid (name, function, height) VALUES ('Battle Droid' , 'Substituir humanos no campo de batalha, usando a quantidade em seu favor', 1.93);
INSERT INTO droid (name, function, height) VALUES ('Mouse Droid', 'Entregar Mensagens; Guiar Visitantes', 0.25);
```

Agora estamos prontos para começarmos nosso código fonte. Vamos iniciar acrescentando a anotação responsável por habilitar a utilização do `ApiJpaRepository` na classe principal da aplicação:

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
```

E agora vamos criar a classe `Droid`, que será responsável para criação do nosso objeto e tabela no banco de dados, e insira o seguinte fonte:

```java
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Droid {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String function;
  private double height;

}
```

Após a criação do modelo, criaremos nossa classe de repositório, que será responsável por acessar o banco de dados e realizar nossas consultas com a técnica de _specification_:

```java
@Repository
@Transactional
public interface DroidRepository extends JpaRepository<Droid, Integer>, ApiJpaRepository<Droid> {
}
```

Com isso temos acesso ao nosso banco de dados de forma simples, para provar isso criaremos nossa API REST que usaremos para realizar essas buscas:

```java
@RestController
@RequestMapping(path = DroidController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class DroidController {

  public static final String PATH = "api/v1/droid";

  @Autowired
  private DroidRepository droidRepository;

  @GetMapping(path = "/findAll")
  @ResponseStatus(code = HttpStatus.OK)
  public List<Droid> findAllDroids() {
    return droidRepository.findAll();
  }

}
```

Feito isso estamos prontos para testar o que fizemos até agora. Inicie sua aplicação Spring, quando a mesma estiver iniciada faça a seguinte requisição:

```http
GET /api/v1/droid/findAll HTTP/1.1
Host: localhost:8080
```

O resultado que esperamos é o seguinte:

```json
[
  {
    "id": 1,
    "name": "Super Battle Droid",
    "function": "Super Soldado de Batalha",
    "height": 1.91
  },
  {
    "id": 2,
    "name": "Protocol Droid",
    "function": "Auxiliam diplomantas e políticos; Programados em etiqueta e equipados com formidáveis ​​habilidades linguísticas",
    "height": 1.7
  },
  {
    "id": 3,
    "name": "Sith Probe Droid",
    "function": "Rastrear fugitivos",
    "height": 0.3
  },
  {
    "id": 4,
    "name": "Battle Droid",
    "function": "Substituir humanos no campo de batalha, usando a quantidade em seu favor",
    "height": 1.93
  },
  {
    "id": 5,
    "name": "Mouse Droid",
    "function": "Entregar Mensagens; Guiar Visitantes",
    "height": 0.25
  }
]
```

Observe que essa requisição trouxe todos os dados que criamos no script `data.sql`, ou seja, está funcionando. Porém agora queremos aplicar alguns filtros para reduzir essa busca, e para isso escolhemos utilizar a técnica de _specification_.

Então vamos criar nossas regras que utilizem o _specification_ criando a classe `DroidSpecification` e nela criaremos 4 modelos de pesquisa, porém existem vários outros que podem ser explorados.

O primeiro modelo que criaremos será utilizando a função de comparação **equal**, onde iremos filtrar os Droids por nome, para isso crie o seguinte método:

```java
public static Specification<Droid> nameEq(String name) {
  return (root, query, cb) -> cb.equal(root.get("name"), name);
}
```

O segundo modelo que aplicaremos será utilizando função **like**, dessa forma iremos filtrar os Droids por suas funções, da seguinte forma:

```java
public static Specification<Droid> functionLike(String function) {
  return (root, query, cb) -> cb.like(cb.upper(root.get("function")), likeConstructor(function));
}

// Método responsável por inserir o carácter % para utilização da função like
private static String likeConstructor(String term) {
  return new StringBuilder().append('%').append(term.trim().toUpperCase()).append('%').toString();
}
```

O terceiro modelo que iremos aplicar, utiliza a função **between** dessa forma filtraremos nossos Droids pela sua altura, conforme o seguinte método:

```java
public static Specification<Droid> heightBetween(double from, double util) {
  return (root, query, cb) -> cb.between(root.get("height"), from, util);
}
```

E para finalizar vamos realizar uma consulta utilizando uma **sub query**, não se assuste pois será muito fácil realizar essa consulta, basta criar o seguinte método:

```java
public static Specification<Droid> droidExists(double height) {
  return (root, query, cb) -> {
    Subquery<Droid> subQuery = query.subquery(Droid.class);
    Root<Droid> subRoot = subQuery.from(Droid.class);
    return cb.exists(subQuery.select(subRoot).where(cb.le(subRoot.get("height"), height),
        cb.equal(subRoot.get("id"), root.get("id"))));
  };
}
```

Feito isso precisamos testar e ver se esses métodos vão realizar os filtros de forma correta, com isso iremos criar novas requisições que utilizem os métodos do _specification_.

## Vamos testar?

Para testarmos nossa API REST, iremos criar 4 novas requisições que irão utilizar os filtros de _specification_. Para validar o filtro pro nome, utilize a seguinte busca para testar:

```java
@GetMapping(path = "/findByName/{name}")
@ResponseStatus(code = HttpStatus.OK)
public ApiCollectionResponse<Droid> findDroidByName(@PathVariable("name") String name) {
  ApiPageRequest pageRequest = new ApiPageRequest();
  Specification<Droid> specs = Specification.where(DroidSpecification.nameEq(name));
  return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
}
```

Realize a seguinte requisição:

```http
GET /api/v1/droid/findByName/Battle Droid HTTP/1.1
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
{
  "hasNext": false,
  "items": [
    {
      "id": 4,
      "name": "Battle Droid",
      "function": "Substituir humanos no campo de batalha, usando a quantidade em seu favor",
      "height": 1.93
    }
  ]
}
```

Nossa próxima busca será baseada no filtro de descrição, para isso insira o seguinte método:

```java
@GetMapping(path = "/findLike/{function}")
@ResponseStatus(code = HttpStatus.OK)
public ApiCollectionResponse<Droid> findDroidLikeDescription(@PathVariable("function") String function) {
  ApiPageRequest pageRequest = new ApiPageRequest();
  Specification<Droid> specs = Specification.where(DroidSpecification.functionLike(function));
  return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
}
```

Realize a seguinte requisição:

```http
GET /api/v1/droid/findLike/etiqueta HTTP/1.1
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
{
  "hasNext": false,
  "items": [
    {
      "id": 2,
      "name": "Protocol Droid",
      "function": "Auxiliam diplomantas e políticos; Programados em etiqueta e equipados com formidáveis ​​habilidades linguísticas",
      "height": 1.7
    }
  ]
}
```

Para nossa próxima busca iremos realizar o filtro pela altura dos Droids, para isso insira o seguinte método:

```java
@GetMapping(path = "/findBetween")
@ResponseStatus(code = HttpStatus.OK)
public ApiCollectionResponse<Droid> findDroidBetweenHeight(@RequestHeader(name = "from") double from,
    @RequestHeader(name = "util") double util) {
  ApiPageRequest pageRequest = new ApiPageRequest();
  Specification<Droid> specs = Specification.where(DroidSpecification.heightBetween(from, util));
  return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
}
```

Realize a seguinte requisição:

```http
GET /api/v1/droid/findBetween HTTP/1.1
from: 1
util: 2
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
{
  "hasNext": false,
  "items": [
    {
      "id": 1,
      "name": "Super Battle Droid",
      "function": "Super Soldado de Batalha",
      "height": 1.91
    },
    {
      "id": 2,
      "name": "Protocol Droid",
      "function": "Auxiliam diplomantas e políticos; Programados em etiqueta e equipados com formidáveis ​​habilidades linguísticas",
      "height": 1.7
    },
    {
      "id": 4,
      "name": "Battle Droid",
      "function": "Substituir humanos no campo de batalha, usando a quantidade em seu favor",
      "height": 1.93
    }
  ]
}
```

E para finalizar utilizaremos a busca de sub query criada, para isso insira o seguinte método:

```java
@GetMapping(path = "/findExists/{height}")
@ResponseStatus(code = HttpStatus.OK)
public ApiCollectionResponse<Droid> findDroidExists(@PathVariable("height") double height) {
  ApiPageRequest pageRequest = new ApiPageRequest();
  Specification<Droid> specs = Specification.where(DroidSpecification.droidExists(height));
  return ApiCollectionResponse.from(droidRepository.findAll(pageRequest, specs));
}
```

Realize a seguinte requisição:

```http
GET /api/v1/droid/findExists/0.29 HTTP/1.1
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
{
  "hasNext": false,
  "items": [
    {
      "id": 5,
      "name": "Mouse Droid",
      "function": "Entregar Mensagens; Guiar Visitantes",
      "height": 0.25
    }
  ]
}
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **API JPA**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-api-jpa) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
