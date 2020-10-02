# Exemplo de uso do componente Tenant Schema

## Contexto

Para exemplificar o uso da biblioteca **Tenant Discriminator** criaremos uma API REST que possibilite a manutenção e leitura dos habitantes do universo **Star Wars**, de forma que estes sejam criados dentro de seus respectivos planetas, que servirão como nossos _tenants_.

## Começando

Iniciaremos o desenvolvimento criando um novo projeto Spring utilizando o serviço [Spring Initializr][spring-initializr]. Precisamos adicionar como dependência os módulos **Spring Web Starter**, **Spring Data JPA**, **Flyway** e o **Lombok**.

Após informados os dados e incluídas as dependências necessárias, podemos iniciar a geração do projeto.

## Configurações

Após gerado, precisamos substituir no arquivo `pom.xml` o _parent_ do projeto pela biblioteca **TJF Boot Starter**:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
</parent>
```

Incluiremos também a dependência para utilização da biblioteca **Tenant Schema** e as configurações do repositório Maven com a distribuição do TJF:

**Dependências**

```xml
<dependencies>
  ...

  <!-- TJF -->
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-tenant-schema</artifactId>
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

```yaml
spring:
  # Configurações banco de dados
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb
    username: sa

  # Configurações JPA
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

> A propriedade `spring.jpa.properties.hibernate.session_factory.statement_inspector` com o valor `com.totvs.tjf.tenant.discriminator.SQLInspector` é necessária para que a biblioteca **Tenant Discriminator** possa interceptar as consultas no banco de dados e incluir como filtro o valor do _tenant_ atual do contexto.

Precisamos também dos _scripts_ de criação dos _schemas_ e tabelas no banco de dados. Estes _scripts_ devem ficar na pasta `src/main/resources/db/migration` com o nome `V1.0__initialize.sql` para que seja feita a execução automática pelo Flyway:

**V1.0__initialize.sql**

```sql
-- TATOOINE
CREATE SCHEMA _TATOOINE;
SET SCHEMA _TATOOINE;
CREATE TABLE habitant
(
   id VARCHAR (36) NOT NULL,
   name VARCHAR (255) NOT NULL,
   gender VARCHAR (06) NOT NULL,
   PRIMARY KEY (id)
);

-- ALDERAAN
CREATE SCHEMA _ALDERAAN;
SET SCHEMA _ALDERAAN;
CREATE TABLE habitant
(
   id VARCHAR (36) NOT NULL,
   name VARCHAR (255) NOT NULL,
   gender VARCHAR (06) NOT NULL,
   PRIMARY KEY (id)
);

-- BESPIN
CREATE SCHEMA _BESPIN;
SET SCHEMA _BESPIN;
CREATE TABLE habitant
(
   id VARCHAR (36) NOT NULL,
   name VARCHAR (255) NOT NULL,
   gender VARCHAR (06) NOT NULL,
   PRIMARY KEY (id)
);
```

### Modelo de dados

Após o desenvolvimento dos _scripts_ de criação das tabelas, precisamos criar as classes que representam cada uma das entidades do nosso banco de dados. As classes de entidade devem ser anotadas com `@Entity` e devem possuir as colunas que a entidade possui (representadas pelos atributos da classe).

#### Entidades

Para iniciar, criaremos a classe de entidade que representa a tabela `habitant`:

**HabitantModel.java**

```java
@Getter
@Setter
@Entity
@Table(name = "habitant")
public class HabitantModel {

  @Id
  private String id;

  @NotNull
  private String name;

  @NotNull
  private String gender;

}
```

Observando a entidade desenvolvida acima, cada habitante terá sua identificação, nome e gênero.

#### Repositories

Após criadas as classes da entidade, criaremos nosso repositório responsável pela criação e leitura dos registros da tabela `habitant` no banco de dados:

**HabitantModelRepository.java**

```java
@Repository
public interface HabitantModelRepository extends JpaRepository<HabitantModel, HabitantModelId> {
}
```

### DTOs

Antes de iniciar a criação da nossa API REST, precisamos criar as classes de [_Data Transfer Object (DTO)_][dto]. Estas classes possibilitam a conversão dos dados recebidos na execução da API REST (geralmente uma estrutura no formato `JSON`) para o formato definido em nosso modelo de dados.

**HabitantDto.java**

```java
@Getter
@Setter
public class HabitantDto {

  private String id;
  private String name;
  private String gender;

}
```

## API REST

Vamos agora desenvolver nossa API REST para criação e leitura dos habitantes:

**HabitantController.java**

```java
@RestController
@RequestMapping(path = "/api/v1/habitants", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitantController {

  @Autowired
  private HabitantModelRepository repository;

  @PostMapping
  public List<HabitantModel> saveAll(@RequestBody List<HabitantDto> dtos) {
    List<HabitantModel> habitants = new ArrayList<>();

    for (HabitantDto dto : dtos) {
      // Efetua a conversão do objeto recebido para o objeto de modelo.
      HabitantModel habitant = new HabitantModel();
      habitant.setId(new HabitantModelId(dto.getId()));
      habitant.setName(dto.getName());
      habitant.setGender(dto.getGender());
      habitants.add(habitant);
    }

    return repository.saveAll(habitants);
  }

  @GetMapping
  public List<HabitantModel> getAll() {
    return repository.findAll();
  }

}
```

A estrutura `JSON` para a criação de um habitante deve ser conforme abaixo:

```json
[
  {
    "id": "luke",
    "name": "Luke Skywalker",
    "gender": "male"
  }
]
```

Porém precisamos ainda definir qual o planeta (_tenant_) do habitante que está sendo criado ou lido.

## Interceptor

A informação do planeta dos habitantes deve ser informada na _header_ customizada `X-Planet`. Para que seja possível recuperar e definir esta informação como _tenant_ criaremos um interceptador de requisições:

**TenantInterceptor.java**

```java
public class TenantInterceptor extends HandlerInterceptorAdapter {
  @Override
  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
    String tenant = req.getHeader("X-Planet");
    Authentication.setAuthenticationInfo(tenant);
    return true;
  }
}
```

E no mesmo pacote devemos criar uma classe de configuração para que o interceptador acima possa ser executado:

**TenantConfigurator.java**

```java
@Configuration
public class TenantConfigurator implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TenantInterceptor());
  }
}
```

Por fim, criaremos a classe de autenticação que será responsável por definir o _tenant_ atual no contexto da aplicação:

**TenantAuthentication.java**

```java
public class TenantAuthentication {

  public static void setAuthenticationInfo(String tenant) {
    SecurityPrincipal principal = new SecurityPrincipal("92e8a7dc-61d8-4045-9d80-222c774ad791", "admin", tenant,
        tenant);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
```

> Em uma aplicação real, a informação do _tenant_ geralmente é informada no _token_ de autenticação.

## Vamos testar?

Após finalizado o desenvolvimento das APIs REST podemos executar nosso projeto, como um **Spring Boot App**, e iniciar a criação dos registros conforme a figura dos personagens abaixo:

<p align="center">
  <img alt="Universo Star Wars" src="./resources/swgalaxy.png"/>
</p>

- Os habitantes **Anakin**, **Luke** e **Han** vivem no planeta **Tatooine**;
- A habitante **Leia** vive no planea **Alderaan**; e
- Os habitantes **Lando** e **Dengar** vivem no planeta **Bespin**.

Vamos criar cada um destes personagens. Para isto basta efetuar uma requisição _HTTP POST_ para cada um deles conforme as informações abaixo::

**Tatooine**

```http
POST /api/v1/habitants HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Planet: Tatooine

[{
  "id": "anakin",
  "name": "Anakin Skywalker",
  "gender": "male"
},{
  "id": "luke",
  "name": "Luke Skywalker",
  "gender": "male"
},{
  "id": "han",
  "name": "Han Solo",
  "gender": "male"
}]
```

**Alderaan**

```http
POST /api/v1/habitants HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Planet: Alderaan

[{
  "id": "leia",
  "name": "Leia Organa",
  "gender": "female"
}]
```

**Bespin**

```http
POST /api/v1/habitants HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Planet: Bespin

[{
  "id": "lando",
  "name": "Lando Calrissian",
  "gender": "male"
},{
  "id": "dengar",
  "name": "Dengar, The Bounty Hunter",
  "gender": "male"
}]
```

> É importante que cada requisição possua a _header_ `X-Planet` com o nome do planeta de cada habitante que está sendo criado.

### Leitura dos registros

Para efetuar a leitura dos habitantes, basta informar na _header_ `X-Planet` o nome do planeta, exemplo:

```http
GET /api/v1/habitants HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Planet: Bespin
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **Tenant Discriminator**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-tenant-discriminator) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).

[spring-initializr]: https://start.spring.io
[dto]: https://pt.stackoverflow.com/questions/31362/o-que-é-um-dto
