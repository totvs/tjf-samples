# Filtro Simples e Complexo da API JPA

# Contexto

Para exemplificar o uso do __Filtro Simples__ e do __Filtro Complexo__ da __API JPA__ vamos criar uma aplicação que contêm duas entidades (empregado e conta) e vamos expor as mesmas via REST.

> É muito importante que, antes de iniciar o desenvolvimento deste _sample_, você tenha lido e compreendido o _sample_ do [__API JPA__][tjf-api-samples-jpa].


## Dependências

Nosso exemplo precisa das seguintes dependências no arquivo `pom.xml` do projeto:

```xml
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

<!-- Spring -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>

<!-- ODATA -->
<dependency>
	<groupId>org.apache.olingo</groupId>
	<artifactId>olingo-odata2-jpa-processor-core</artifactId>
</dependency>
```

*** Ressaltamos aqui o uso do [Apache Olingo][apache-olingo], mecanismo utilizado pelo TJF na implementação do Filtro Complexo. 


## Filtro Simples

Para expor uma entidade através do Filtro Simples basta implementar nela, a interface SimpleFilterSupport, conforme exemplo abaixo: 

_AccountModel.java_

```java
@Entity
@Table(name = "cash_account")
@SimpleFilterSupportDeny
public class AccountModel implements SimpleFilterSupport <AccountModel> {
```

Ou então em uma classe que contenha os campos passíveis de pesquisa:

_AccountSimpleFilter.java_

```java
public class AccountSimpleFilter implements SimpleFilterSupport <AccountModel> {

	private static final long serialVersionUID = 5118512538332202507L;
	
	public BigDecimal limit;
	public Employee employee = new Employee();
	
	public AccountSimpleFilter () {}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
...
```

Para expor a pesquisa via REST, basta adicionar como parâmetro uma das classes acima no método mapeado, repassando a mesma para algum método do repositório JPA que suporte especificações:

_SFController.java_

```java
@RestController
@ApiGuideline(ApiGuidelineVersion.v1)
@RequestMapping("/api/v1")
public class SFController {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;

	@GetMapping(path = "/accounts", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse<AccountModel> getAllAccounts(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, AccountModel simpleFilter) {
		return accountRepos.findAllProjected(field, page, sort, simpleFilter);
	}

	@GetMapping(path = "/employees", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse<EmployeeModel> getAllEmployess(ApiFieldRequest field, ApiPageRequest page,
			ApiSortRequest sort, EmployeeModel simpleFilter) {
		return employeeRepos.findAllProjected(field, page, sort, simpleFilter);
	}

}
```

Para testar os endpoints acima basta requisitar uma das URLs, informado os campos passíveis de pesquisa, conforme exemplos abaixo: 

- [http://localhost:8380/api/v1/accounts](http://localhost:8380/api/v1/accounts)
- [http://localhost:8380/api/v1/employees?name=John](http://localhost:8380/api/v1/employees?name=John)


## Filtro Complexo

O Filtro Complexo, é ainda mais simples de ser utilizado.

Para expor a pesquisa via REST, basta adicionar como parâmetro a class _ApiComplexFilterRequest_ no método mapeado, repassando o mesma o método _getResponse_ da classe _ComplexFilterRepository_, conforme exemplo abaixo:


_CFController.java_

```java
@RestController
@ApiGuideline(ApiGuidelineVersion.v1)
@RequestMapping("/api/v1")
public class CFController {

	@Autowired
	private ComplexFilterRepository complexFilterRepos;
	
	@GetMapping(path = "/contas", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse <JsonNode> getContas(ApiComplexFilterRequest filter,
			ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return complexFilterRepos.getResponse(AccountModel.class, filter, field, page, sort);
	}

	@GetMapping(path = "/empregados", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ApiCollectionResponse <JsonNode> getEmpregados(ApiComplexFilterRequest filter,
			ApiFieldRequest field, ApiPageRequest page, ApiSortRequest sort) {
		return complexFilterRepos.getResponse(EmployeeModel.class, filter, field, page, sort);
	}
	
}
```

Para testar os endpoints acima basta requisitar uma das URLs, informado os campos passíveis de pesquisa, conforme exemplos abaixo: 

- [http://localhost:8380/api/v1/contas](http://localhost:8380/api/v1/contas)
- [http://localhost:8380/api/v1/empregados?$filter=name eq 'John'](http://localhost:8380/api/v1/empregados?$filter=name eq 'John')


# Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente __API JPA Filters__ e enviar sugestões e melhorias para o projeto __TOTVS Java Framework__.

> O conteúdo deste _sample_ está em nosso repositório do [GitHub][github].


[tjf-api-context-sample]: https://github.com/totvs/tjf-api-context-sample
[guia-api-totvs]: http://tdn.totvs.com/x/nDUxE
[h2]: https://www.h2database.com
[hibernate]: https://hibernate.org
[spring]: https://spring.io
[tjf-api-samples-jpa]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[apache-olingo]: https://olingo.apache.org/
[github]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-filters-sample
