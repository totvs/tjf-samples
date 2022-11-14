# Exemplo de uso do componente API JPA (Query By Example)

Exemplo para utilização de _Query by Example_ com o módulo **API JPA**.

## Contexto

Para exemplificar o uso da técnica _Query by Example_, criaremos um exemplo para busca de Droids, filtrando estes por suas características.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Na geração da aplicação adicione como dependência as bibliotecas **Spring Starter Web**, **H2** e o **Lombok**.

## Configurações

Além das dependências do Spring mencionadas acima, para utilização do componente é necessário alterar o `parent` da aplicação no arquivo `pom.xml`:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>3.14.5-RELEASE</version>
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
  private Integer id;
  private String name;
  private String function;
  private Double height;

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

O primeiro modelo que criaremos será utilizando a função de comparação **EQUAl**, onde iremos filtrar os Droids por nome, para isso crie o seguinte método:

```java
@GetMapping(path = "/findByName/{name}")
@ResponseStatus(code = HttpStatus.OK)
public List<Droid> findDroidByName(@PathVariable("name") String name) {
  Droid droid = new Droid();
  droid.setName(name);

  Example<Droid> example = Example.of(droid);

  return droidRepository.findAll(example);
}
```

O segundo modelo que aplicaremos será utilizando função **CONTAINING**, dessa forma iremos filtrar os Droids por suas funções, da seguinte forma:

```java
@GetMapping(path = "/findContainingFunction/{function}")
@ResponseStatus(code = HttpStatus.OK)
public List<Droid> findDroidLikeDescription(@PathVariable("function") String function) {
  Droid droid = new Droid();
  droid.setFunction(function);

  ExampleMatcher matcher = ExampleMatcher.matchingAll()
      .withStringMatcher(StringMatcher.CONTAINING)
      .withIgnoreCase();
  Example<Droid> example = Example.of(droid, matcher);

  return droidRepository.findAll(example);
}
```

Feito isso precisamos testar e ver se esses métodos vão realizar os filtros de forma correta.

## Vamos testar?

Para testarmos nossa API REST, realize a seguinte requisição:

```http
GET /api/v1/droid/findByName/Battle Droid HTTP/1.1
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
[
  {
    "id": 4,
    "name": "Battle Droid",
    "function": "Substituir humanos no campo de batalha, usando a quantidade em seu favor",
    "height": 1.93
  }
]
```

E para finalizar nossa próxima busca será baseada no filtro de descrição, para isso realize a seguinte requisição:

```http
GET /api/v1/droid/findContainingFunction/batalha HTTP/1.1
Host: localhost:8080
```

E teremos o seguinte retorno:

```json
[
  {
    "id": 1,
    "name": "Super Battle Droid",
    "function": "Super Soldado de Batalha",
    "height": 1.91
  },
  {
    "id": 4,
    "name": "Battle Droid",
    "function": "Substituir humanos no campo de batalha, usando a quantidade em seu favor",
    "height": 1.93
  }
]
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **API JPA**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-api-jpa) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
