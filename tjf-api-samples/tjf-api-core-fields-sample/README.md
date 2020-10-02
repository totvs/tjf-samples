# Exemplo de uso do componente API Core (Fields)

Exemplo para utilização de filtro de resposta utilizando o atributo _fields_ do módulo **API Core**.

## Contexto

Para exemplificar o uso de _fields_ com o uso da biblioteca **API Core**, faremos um exemplo de busca de Jedis, onde filtraremos as informações que queremos.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Na geração da aplicação adicione como dependência as bibliotecas **Spring WEB** e o **Lombok**.

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
  <artifactId>tjf-api-core</artifactId>
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

E vamos configurar o uso dos _fields_ conforme documentação do módulo **API Core**, para isso no seu arquivo `application.yml` insira as propriedades:

```yaml
tjf:
  api:
    filter:
      fields:
        enabled: true
```

## Codificando

Agora temos todas configurações necessárias e podemos começar nosso código fonte, então vamos começar pela classe modelo:

```java
@Getter
@Setter
@AllArgsConstructor
public class Jedi {

  private String name;
  private String gender;
  private String species;
  private double height;

}
```

Feito isso vamos criar nossa API REST `JediController`, ela será responsável por expor o _endpoint_ que usaremos o componente _fields_.

```java
@RestController
@RequestMapping(path = JediController.PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class JediController {

  public static final String PATH = "api/v1/jedi";

  @GetMapping(path = "/find")
  @ResponseStatus(code = HttpStatus.OK)
  public ApiCollectionResponse<Jedi> nameGet() throws IOException {
    List<Jedi> jedis = List.of(new Jedi("Luke Skywalker", "Masculino", "Humano", 1.72),
        new Jedi("Anakin Skywalker", "Masculino", "Humano", 1.88),
        new Jedi("Obi-Wan Kenobi", "Masculino", "Humano", 1.82),
        new Jedi("Mace Windu", "Masculino", "Humano", 1.92),
        new Jedi("Yoda", "Masculino", "Desconhecida", 0.66));
    return ApiCollectionResponse.from(jedis);
  }

}
```

## Vamos testar?

Para testar tudo o que fizemos é simples, basta criar chamadas do método `GET` para a url `http://localhost:8080/api/v1/jedi/find` da seguinte forma:

```http
GET /api/v1/jedi/find HTTP/1.1
Host: localhost:8080
```

Feito essa requisição teremos o seguinte resultado:

```json
{
  "hasNext": false,
  "items": [
    {
      "name": "Luke Skywalker",
      "gender": "Masculino",
      "species": "Humano",
      "height": 1.72
    },
    {
      "name": "Anakin Skywalker",
      "gender": "Masculino",
      "species": "Humano",
      "height": 1.88
    },
    {
      "name": "Obi-Wan Kenobi",
      "gender": "Masculino",
      "species": "Humano",
      "height": 1.82
    },
    {
      "name": "Mace Windu",
      "gender": "Masculino",
      "species": "Humano",
      "height": 1.92
    },
    {
      "name": "Yoda",
      "gender": "Masculino",
      "species": "Desconhecida",
      "height": 0.66
    }
  ]
}
```

Mas ainda não é esse resultado que queremos ver, vamos filtrar esse retorno para trazer somente o que queremos, para isso execute a seguinte chamada:

```http
GET /api/v1/jedi/find?fields=name,gender HTTP/1.1
Host: localhost:8080
```

E agora sim teremos o resultado filtrado, observe que neste utilizamos os atributos _name_ e _gender_, mas você pode utilizar qualquer atributo do modelo Jedi:

```Json
{
  "hasNext": false,
  "items": [
    {
      "name": "Luke Skywalker",
      "gender": "Masculino"
    },
    {
      "name": "Anakin Skywalker",
      "gender": "Masculino"
    },
    {
      "name": "Obi-Wan Kenobi",
      "gender": "Masculino"
    },
    {
      "name": "Mace Windu",
      "gender": "Masculino"
    },
    {
      "name": "Yoda",
      "gender": "Masculino"
    }
  ]
}
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **API Core**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-api-core) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
