# Exemplo de uso do componente API JPA (Filtros Simples e Complexo)

## Contexto

Para exemplificar o uso de filtro simples e complexo do **Api JPA** vamos criar uma aplicação que contêm duas entidades (empregado e conta) e vamos expor as mesmas via REST.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Na geração da aplicação adicione como dependência as bibliotecas **Spring WEB**, **Starter Data JPA**, **H2** e o **Lombok**.

## Dependências

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
<!-- ODATA -->
<dependency>
  <groupId>org.apache.olingo</groupId>
  <artifactId>olingo-odata2-jpa-processor-core</artifactId>
</dependency>

<!-- TJF -->
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-core-money</artifactId>
</dependency>

<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-core</artifactId>
</dependency>

<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-jpa</artifactId>
</dependency>
```

> Ressaltamos aqui o uso do [Apache Olingo][apache-olingo], mecanismo utilizado na implementação do filtro complexo.

Vamos adicionar também o repositório Maven de release do TJF:

```xml
<repositories>
  <repository>
    <id>central-release</id>
    <name>TOTVS Java Framework: Releases</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release/</url>
  </repository>
</repositories>
```

## Filtro simples

Para expor uma entidade através do filtro simples basta implementar nela a interface `SimpleFilterSupport`, conforme exemplo abaixo:

**AccountModel.java**

```java
@Entity
@Table(name = "cash_account")
@SimpleFilterSupportDeny
public class AccountModel implements SimpleFilterSupport<AccountModel> {}
```

Ou então criar uma classe que contenha os campos passíveis de pesquisa:

**AccountSimpleFilter.java**

```java
@Getter
@Setter
public class AccountSimpleFilter implements SimpleFilterSupport<AccountModel> {

  private static final long serialVersionUID = 5118512538332202507L;

  private BigDecimal limit;
  private Employee employee = new Employee();

  @Getter
  @Setter
  public class Employee {
    private String name;
  }

}
```

Para expor a pesquisa via API REST, basta adicionar como parâmetro uma das classes acima no método mapeado, repassando a mesma para algum método do repositório JPA que suporte especificações:

**SFController.java**

```java
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class SFController {

  @Autowired
  private EmployeeModelRepository employeeRepos;

  @Autowired
  private AccountModelRepository accountRepos;

  @GetMapping(path = "/accounts")
  public ApiCollectionResponse<AccountModel> getAllAccounts(ApiFieldRequest field, ApiPageRequest page,
      ApiSortRequest sort, AccountModel simpleFilter) {
    return ApiCollectionResponse.from(accountRepos.findAllProjected(field, page, sort, simpleFilter));
  }

  @GetMapping(path = "/employees")
  public ApiCollectionResponse<EmployeeModel> getAllEmployess(ApiFieldRequest field, ApiPageRequest page,
      ApiSortRequest sort, EmployeeModel simpleFilter) {
    return ApiCollectionResponse.from(employeeRepos.findAllProjected(field, page, sort, simpleFilter));
  }

}
```

Para testar os endpoints acima basta requisitar uma das URLs, informado os campos passíveis de pesquisa, conforme exemplos abaixo:

- `http://localhost:8380/api/v1/accounts`
- `http://localhost:8380/api/v1/employees?name=John`

## Filtro Complexo

O filtro Complexo é ainda mais fácil de ser utilizado. Para expor a pesquisa via API REST, basta adicionar como parâmetro a classe `ApiComplexFilterRequest` no método mapeado, repassando o mesma para o método `getResponse` da classe `ComplexFilterRepository`, conforme exemplo abaixo:

**CFController.java**

```java
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class CFController {

  @Autowired
  private ComplexFilterRepository complexFilterRepos;

  @GetMapping(path = "/contas")
  public ApiCollectionResponse<JsonNode> getContas(ApiComplexFilterRequest filter, ApiFieldRequest field,
      ApiPageRequest page, ApiSortRequest sort) {
    var response = complexFilterRepos.getResponse(AccountModel.class, filter, field, page, sort);
    return ApiCollectionResponse.from(response);
  }

  @GetMapping(path = "/empregados")
  public ApiCollectionResponse<JsonNode> getEmpregados(ApiComplexFilterRequest filter, ApiFieldRequest field,
      ApiPageRequest page, ApiSortRequest sort) {
    var response = complexFilterRepos.getResponse(EmployeeModel.class, filter, field, page, sort);
    return ApiCollectionResponse.from(response);
  }

}
```

Para testar os endpoints acima basta requisitar uma das URLs, informado os campos passíveis de pesquisa, conforme exemplos abaixo:

- `http://localhost:8380/api/v1/contas`
- `http://localhost:8380/api/v1/empregados?\$filter=name eq 'John'`


## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **API JPA**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-api-jpa) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).

[apache-olingo]: https://olingo.apache.org/
