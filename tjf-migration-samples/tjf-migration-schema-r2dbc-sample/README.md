# Exemplo de uso do componente Migration Schema

## Contexto

Para exemplificar o uso da biblioteca **Migration Schema**, criaremos uma classe simples de teste, para validar que os _schemas_ serão criados da forma esperada pela ferramenta de migração.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto. Precisamos adicionar como dependência os módulos **Spring Starter**, **Flyway** e o **H2**.

## Configurações

Após gerado o projeto, vamos adicionar a dependência do **Migration Schema** e o repositório Maven de _release_ do TJF:

**Dependências**

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

**Repositório**

```xml
<repositories>

  <repository>
    <id>central-release</id>
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
    schemas: TATOOINE, ALDERAAN, BESPIN
    baselineOnMigrate: true

  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:starwarsdb;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE;
    username: sa
```

Nas configurações acima, definimos qual _driver_ será utilizado para conexão com o banco de dados, o nome do banco e usuário de acesso.

Observe que no trecho abaixo estamos informando os _schemas_ que serão criados: **TATOOINE**, **ALDERAAN** e **BESPIN**.

```yml
flyway:
  enabled: false
  migrate: true
  locations: classpath:db/migration
  schemas: TATOOINE, ALDERAAN, BESPIN
  baselineOnMigrate: true
```

Precisamos também do _scripts_ de criação da tabela no banco de dados. Este _script_ deve ficar na pasta `src/main/resources/db/migration` com o nome `V1.0__initialize.sql` para que seja feita a execução automática pelo Flyway:

**V1.0__initialize.sql**

```sql
CREATE TABLE habitant
(
   id     VARCHAR (036) NOT NULL,
   name   VARCHAR (255) NOT NULL,
   gender VARCHAR (006) NOT NULL,
   PRIMARY KEY (id)
);
```

> OBS: diferentemente do _sample_ do **Tenant Schema**, não precisamos criar e definir os _schemas_ nesse arquivo, pois o **Migration Schema** fará este processo com base no arquivo `application.yml`.

## Execução dos testes

Para comprovar que os _schemas_ foram criados, criamos uma classe com alguns alguns testes simples:

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

> Vale lembrar que os _schemas_ foram criados com o caractere _underscore_ na frente do nome, pois este é o padrão de nomenclatura atual do TJF.

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **Migration Schema**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-migration-schema) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
