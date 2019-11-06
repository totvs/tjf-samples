# Tenant Schema Sample

_Sample_ de utilização da biblioteca [__Migration Schema__][tjf-migration-schema] do [__TOTVS Java Framework__][tjf].

## Contexto

Para exemplificar o uso da biblioteca [__Migration Schema__][tjf-migration-schema], criaremos uma aplicação simples de teste, para validar que os _schemas_ serão criados da forma esperada.

> Como _engine_ de banco de dados utilizaremos o [H2][h2].

# Começando

Iniciaremos o desenvolvimento criando um novo projeto [Spring][spring] utilizando o serviço [Spring Initializr][spring-initializr]. O projeto deve possuir as configurações conforme abaixo:

<p align="center">
  <img alt="Spring Initializr" src="./resources/spring-initializr.png"/>
</p>

Precisamos também adicionar como dependência os módulos __Web__, __Flyway__ e __H2__. Após informados os dados e incluídas as dependências necessárias, podemos iniciar a geração do projeto.


## Configurações

_Dependências_

```xml
<dependencies>
  ...

  <!-- TJF -->
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-migration-schema</artifactId>
  </dependency>

</dependencies>
```

_Repositórios_

```xml
<repositories>

  <repository>
    <id>tjf-release</id>
    <name>TOTVS Java Framework: Releases</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release/</url>
  </repository>

</repositories>
```

### Banco de dados

As configurações do banco de dados devem ser incluídas no arquivo `application.yml`:

```yaml
spring:

  flyway:
    enabled: false
    migrate: true
    locations: classpath:db/migration
    schemas: _TATOOINE, _ALDERAAN, _BESPIN
    baselineOnMigrate: true

  h2:
    console:
      enabled: true
  
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE;
    username: sa
    password:
```

Nas configurações acima, definimos qual _driver_ será utilizado para conexão com o banco de dados, o nome do banco (`starwarsdb`) e usuário de acesso (`sa`).

Vejamos que no trecho a seguir estamos criando os _schemas_: __TATOOINE, ALDERAAN e BESPIN__. 

```yml
  flyway:
    enabled: false
    migrate: true
    locations: classpath:db/migration
    schemas: _TATOOINE, _ALDERAAN, _BESPIN
    baselineOnMigrate: true
```

Precisamos também dos _scripts_ de criação de tabelas no banco de dados. Este _script_ deve ficar na pasta `src/main/resources/db/migration` com o nome `V1.0__initialize.sql` para que seja feita a execução automática pelo [__Flyway__][flyway]:

_V1.0__initialize.sql_

```sql
CREATE TABLE habitant (
  id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  gender VARCHAR(06) NOT NULL,
  PRIMARY KEY (id));
```

> OBS: Diferentemente do sample do _tenant-schema_, não precisaremos criar e setar os _schemas_ nesse arquivo, pois o TJF-Migration-Schema faz com base no arquivo _application.yml_. 


### Execução dos testes

Para comprovar que os _schemas_ foram criados, executaremos alguns testes simples, conforme a seguir:

```java
@Test
public void testSchemas() throws SQLException {
	Connection connection = DriverManager.getConnection("jdbc:h2:mem:starwarsdb", "sa", "");

	PreparedStatement statement = connection.prepareStatement("SET SCHEMA _TATOOINE");
	statement.execute();
	assertEquals("_TATOOINE", connection.getSchema());
	
	PreparedStatement statement2 = connection.prepareStatement("SET SCHEMA _ALDERAAN");
	statement2.execute();
	assertEquals("_ALDERAAN", connection.getSchema());
	
	PreparedStatement statement3 = connection.prepareStatement("SET SCHEMA _BESPIN");
	statement3.execute();
	assertEquals("_BESPIN", connection.getSchema());
}
```

# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__Migration Schema__][tjf-migration-schema] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf-migration-schema]: https://tjf.totvs.com.br/wiki/tjf-migration
[tjf]: https://tjf.totvs.com.br
[h2]: https://www.h2database.com
[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[flyway]: https://flywaydb.org