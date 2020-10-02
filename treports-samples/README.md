# TReports Sample

_Sample_ da utilização do [__TReports__][treports] com aplicação construidas usando o [__TOTVS Java Framework__][tjf]
e com segurança usando o RAC para autenticação.

## Contexto

Uma das maneiras de integrar o [TReports][treports] à aplicação é fazendo acesso a um API, para isso a API deve estar cadastrada no portal [API reference TOTVS][api].

> **Observação:** mais informações de como construir uma API e como disponibilizá-la no portal podem ser encontradas na página [TTalk no TDN][ttalk].

# Começando

Crie um novo _maven project_ e no pom.xml do projeto acrescente o _parent_ tjf-boot-starter bem como nosso repositório de pacotes:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.2.6.0-RELEASE</version>
</parent>

<repositories>
  <repository>
    <id>central-release</id>
    <name>TOTVS Java Framework: Release</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release</url>
  </repository>
</repositories>
```

Ainda no pom.xml acrescente as dependências que iremos precisar:
* **spring-boot-starter-web:** Para fazermos requisições _rest_ para a nossa aplicação.
* **tjf-api-core:** Módulo do TJF que torna nossa API compatível com o padrão de API da TOTVS.
* **tjf-api-jpa:** Módulo do TJF com métodos de consulta a banco de dados e que em conjunto com o tjf-api-core já retorna os dados no padrão de API da TOTVS.
* **tjf-security-web:** Módulo do TJF que implementa segurança em nossa aplicação e a integra a um serviço de autenticação, no nosso exemplo será o RAC.
* **h2:** Um banco de dados em memória que usaremos para fazer a persistência e leitura dos dados.

```xml
<dependencies>
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
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-api-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-security-web</artifactId>
  </dependency>

  <!-- Database -->
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

## Propriedades

Na pasta _resources_ do projeto crie o arquivo `application.yml` com as seguintes propriedades:

```yml
spring:
  datasource:
    url: jdbc:h2:file:~/h2db
    driverClassName: org.h2.Driver
    username: sa
    password: 
    initialization-mode: always
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create

server:
  port: 8880
  
security:
  oauth2:
    resource:
      id: authorization_api
      jwk:
        key-set-uri: http://localhost:5009/totvs.rac/.well-known/openid-configuration/jwks
```

Ainda na pasta, crie o arquivo data.sql. Ele será automaticamente executado para popular nosso banco de dados na inicialização da aplicação.

```sql
insert into COST_CENTER values ('1', '1', 'one', 'active', 'o', true);
insert into COST_CENTER values ('2', '2', 'two', 'inactive', 't', false);
insert into COST_CENTER values ('3', '3', 'three', 'active', 'th', true);
insert into COST_CENTER values ('4', '1', 'four', 'inactive', 'f', false);
insert into COST_CENTER values ('5', '2', 'five', 'active', 'fi', true);
insert into COST_CENTER values ('6', '3', 'six', 'inactive', 's', false);
```

## _Main_

No pacote `com.tjf.samples.treports` crie a classe TreportsApplication, que será nossa classe _main_.

```java
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class TreportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreportsApplication.class, args);
	}
}
```

## Modelo

No nosso exemplo usaremos a definição de uma API simples que já foi disponibilizada no [portal de api][api] - [Centro de Custo - 1.000][apicostcenter]. 

No pacote `com.tjf.samples.treports.model` crie a classe _CostCenter_ que irá representar nosso modelo de `Centro de Custo`:

_CostCenter.java_

```java
@Entity
public class CostCenter {

	@Id
	String code;
	String companyId;
	String registerSituation;
	String name;
	String shortCode;
	boolean sped;

	public CostCenter() {
	}

	public CostCenter(String companyId, String code, Situation situation, String name, String shortCode, boolean sped) {
		this.companyId = companyId;
		this.code = code;
		this.registerSituation = situation.value;
		this.name = name;
		this.shortCode = shortCode;
		this.sped = sped;
	}

	@JsonProperty("CompanyId")
	public String getCompanyId() {
		return companyId;
	}

	@JsonProperty("Code")
	public String getCode() {
		return code;
	}

	@JsonProperty("RegisterSituation")
	public String getRegisterSituation() {
		return registerSituation;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("ShortCode")
	public String getShortCode() {
		return shortCode;
	}

	@JsonProperty("SPED")
	public boolean isSped() {
		return sped;
	}

	public enum Situation {
		ACTIVE("active"), INACTIVE("Inactive");

		public String value;

		Situation(String value) {
			this.value = value;
		}
	}
}
```

Crie também no mesmo pacote o _repository_ que fará as consultas à entidade `CostCenter`.

_CostCenterRepository.java_
```java
public interface CostCenterRepository extends JpaRepository<CostCenter, String>, ApiJpaRepository<CostCenter>{}
```

## API

Crie a classe CostCentersController no pacote `com.tjf.samples.treports.controller`, nela teremos o _endpoint_ `/api/ctb/v1/costcenters`, conforme está definido na API [Centro de Custo - 1.000][apicostcenter].

_CostCentersController.java_
```java
@RestController
@RequestMapping(path = "/api/ctb/v1/costcenters", produces = "application/json")
public class CostCentersController {

	@Autowired
	CostCenterRepository costCenterRepository;

	@GetMapping
	public ApiCollectionResponse<CostCenter> getCostCenters(ApiFieldRequest fieldRequest, ApiPageRequest pageRequest,
			ApiSortRequest sortRequest) {
		return costCenterRepository.findAllProjected(fieldRequest, pageRequest, sortRequest);
	}
}
```

# Ambiente

Usaremos o [Docker][docker] para rodar o TReports. Segue abaixo o docker-compose.yml com as definições necessárias para rodar as imagens do RAC, já configurado para o TReports e para nossa aplicação, do TReports e do banco de dados que precisaremos para execução da nossa instância do TReports e do RAC.

```yml
version: '3.6'

networks:
  docker-network-trep:
    driver: bridge

services:
  treports:
    image: docker.totvs.io/treports/treports:12125
    environment:
      DOCKER_DBPROVIDER: "SqlServer"
      DOCKER_CONNECTIONSTRING: "Server=db;database=TRFDb_Docker;User=sa;Password=012345678@totvs123"
      DOCKER_JOB_DELAYTIME: "1"
      DOCKER_JOB_LOOPINTERATIONS: "10"
      DOCKER_LICENSE_SERVERIP: "10.80.128.51"
      DOCKER_LICENSE_SERVERPORT: "5555"
      DOCKER_HANGFIRE_USEHANGFIRE: "true"
      DOCKER_CLIENTSECRET: "totvs@123"
      DOCKER_AUTHORITYENDPOINT_SERVER: "http://rac/totvs.rac"
      DOCKER_AUTHORITYENDPOINT_CLIENT: "http://localhost:5009/totvs.rac"
      DOCKER_CLIENTID: "js_oidc_treports"
      DOCKER_DEFAULTTENANT: "empresa1"
    ports:
      - "7017:7017"
    networks:
      - docker-network-trep
    depends_on:
      - rac

  rac:
    image: docker.totvs.io/tnf/rac:1.6.4
    environment:
      DefaultSchema: "dbo"
      DefaultConnectionString: "SqlServer"
      ConnectionStrings__SqlServer: "Data Source=db;User Id=sa;Password=012345678@totvs123"
      RunMigrator: "T"
      
      Clients__0__ClientId: "rac_oidc"
      Clients__0__ClientName: "Client OIDC padrão do RAC"
      Clients__0__ProductName: "TOTVS RAC"
      Clients__0__GrantTypes__0: "Hybrid"
      Clients__0__GrantTypes__1: "ClientCredentials"
      Clients__0__ExpirationTimeInMinutes: "20"
      Clients__0__RedirectUrls__0: "http?://rac/totvs.rac/auth-callback"
      Clients__0__RedirectUrls__1: "http?://rac/totvs.rac/assets/silent-renew.html"
      Clients__0__RedirectUrls__2: "http?://localhost:5009/totvs.rac/auth-callback"
      Clients__0__RedirectUrls__3: "http?://localhost:5009/totvs.rac/assets/silent-renew.html"
      Clients__0__RedirectUrls__4: "http?://*.localhost:5009/totvs.rac/auth-callback"
      Clients__0__RedirectUrls__5: "http?://*.localhost:5009/totvs.rac/assets/silent-renew.html"
      Clients__0__LogoutUrls__0: "http?://rac/totvs.rac"
      Clients__0__LogoutUrls__1: "http?://localhost:5009/totvs.rac"
      Clients__0__LogoutUrls__2: "http?://*.localhost:5009/totvs.rac"
      Clients__0__LogoutUrls__3: "http://rac/totvs.rac"
      Clients__0__Secrets__0__Description: "Senha Padrão (totvs@123)"
      Clients__0__Secrets__0__Value: "totvs@123"

      Clients__1__ClientId: "js_oidc_treports"
      Clients__1__ClientName: "js_oidc_treports"
      Clients__1__ProductName: "TReports"
      Clients__1__GrantTypes__0: "Hybrid"
      Clients__1__GrantTypes__1: "ClientCredentials"
      Clients__1__ExpirationTimeInMinutes: "20"
      Clients__1__RedirectUrls__0: "http?://localhost:7017/auth-callback"
      Clients__1__RedirectUrls__1: "http?://localhost:7017/assets/silent-renew.html"
      Clients__1__RedirectUrls__2: "http?://*.localhost:7017/auth-callback"
      Clients__1__RedirectUrls__3: "http?://*.localhost:7017/assets/silent-renew.html"
      Clients__1__LogoutUrls__0: "http?://localhost:7017"
      Clients__1__LogoutUrls__1: "http?://*.localhost:7017"
      Clients__1__Secrets__0__Description: "Senha Padrão (totvs@123)"
      Clients__1__Secrets__0__Value: "totvs@123"

      Clients__2__ClientId: "js_oidc_sampleapp"
      Clients__2__ClientName: "js_oidc_sampleapp"
      Clients__2__ProductName: "SampleApp"
      Clients__2__GrantTypes__0: "Hybrid"
      Clients__2__GrantTypes__1: "ClientCredentials"
      Clients__2__ExpirationTimeInMinutes: "20"
      Clients__2__RedirectUrls__0: "http?://localhost:8880/auth-callback"
      Clients__2__RedirectUrls__1: "http?://localhost:8880/assets/silent-renew.html"
      Clients__2__RedirectUrls__2: "http?://*.localhost:8880/auth-callback"
      Clients__2__RedirectUrls__3: "http?://*.localhost:8880/assets/silent-renew.html"
      Clients__2__LogoutUrls__0: "http?://localhost:8880"
      Clients__2__LogoutUrls__1: "http?://*.localhost:8880"
      Clients__2__Secrets__0__Description: "Senha Padrão (totvs@123)"
      Clients__2__Secrets__0__Value: "totvs@123"

      Clients__3__ClientId: "treports_ro"
      Clients__3__ClientName: "treports_ro"
      Clients__3__ProductName: "TReports"
      Clients__3__GrantTypes__0: "ResourceOwner"
      Clients__3__ExpirationTimeInMinutes: "20"
      Clients__3__Secrets__0__Description: "Senha Padrão (totvs@123)"
      Clients__3__Secrets__0__Value: "totvs@123"

      Tenants__0__TenantName: "empresa1"
      Tenants__0__Name: "Empresa Sample"
      Tenants__0__CNPJ: "53113791001102"
      Tenants__0__Products__0__ProductName: "TOTVS RAC"
      Tenants__0__Products__1__ProductName: "TReports"
      Tenants__0__Products__2__ProductName: "SampleApp"

      HostUrl: "http://*.localhost:5009/totvs.rac"
    ports:
      - "5009:80"
    networks:
      - docker-network-trep
    depends_on:
      - db

  db:
    image: mcr.microsoft.com/mssql/server:2017-CU14-ubuntu
    environment:
      SA_PASSWORD: "012345678@totvs123"
      ACCEPT_EULA: "Y"
    ports:
      - "14336:1433"
    networks:
      - docker-network-trep
```

> :warning: **Observação:** Você precisa solicitar autorização a equipe responsável pela imagem `docker.totvs.io/tnf/rac` para utilizá-la.

Para subir o ambiente do TReports use o comando abaixo:

```sh
$ docker-compose up -d -f 'docker-compose.yml'
```

> **Observação:** Caso queira mais informações e dicas de como subir no _docker_ o TReports e os serviços que ele necessita, você pode encontrar no vídeo [How To TReports - Instalação via Docker][howtotreportsdocker] no canal [How To - TReports][howtotreports] no Youtube.

# Configuração 

Precisamos configurar o RAC antes de continuar, acesse o RAC pela URL http://localhost:5009/, informe _tenant_ `empresa1` usuário `admin` e senha `totvs@123`. Crie um novo perfil e dê permissão a ele nas _features_ do TReports, depois altere o usuário e acrescente a ele o perfil que criamos.

Agora acesse o TReports pela URL http://localhost:7017/, usuário `admin` e senha `totvs@123`. Crie um novo provedor de dados com nome `sampleapp` e informe as seguintes propriedades:

| Propriedade          | Valor                              |
|----------------------|------------------------------------|
| Tipo do provedor     | Api padrão Totvs                   |
| Fonte de dados       | Api Git                            |
| Protocolo            | http                               |
| Host                 | 172.20.0.1¹                        |
| Porta                | 8880                               |
| Tipo de autenticação | OpenID                             |
| Client ID            | js_oidc_tjf                        |
| Client Secret        | totvs@123                          |
| Access Token URL     | http://rac/totvs.rac/connect/token |
| Scope                | authorization_api                  |

> **¹ Observação:** O ip do _host_ no _container_ do TReports pode variar dependendo do seu _docker_, geralmente é `192.168.0.1` ou `172.20.0.1`, mas você pode rodar esse comando para identificar o ip: 

```sh
sudo docker inspect -f '{{range .NetworkSettings.Networks}}{{.Gateway}}{{end}}' <Nome do seu _container_ do treports no 'docker ps'>
```

## Relatório

Vamos precisar de um grupo de relatórios para nosso exemplo. Crie um grupo de relatório informando `TJF` em todos os campos.

Abra relatórios no menu do TReports, importe o arquivo RelCostCenters.trep que se encontra na pasta `relatorios` do projeto, informe o provedor e o grupo de relatório que criamos. Nessa pasta há mais dois relatórios de exemplo: o `RelRetailStockLevel` que faz uso de gráficos e o `RelProduct` que faz uso de imagens.

> **Observação:** Para mais detalhes de como desenvolver o relatório e de como criar o provedor de dados, sugiro o vídeo [TReports - Provedor de API][treportsprovedorapi] da nossa comunidade [Totvs Developers][totvsdevelopers] no Facebook, ele demostra a criação de um relatório com base em um provedor API. Você pode encontrar mais material também nosso canal [How To - TReports][howtotreports] no Youtube.

# Vamos testar!

Para testar, execute nossa aplicação de exemplo e após concluir a inicialização vá no relatório RelCostCenters que importamos e nas opções clique em `Gerar`, ele irá automaticamente realizar a autenticação com o RAC e com nossa aplicação e irá gerar o relatório com os dados retornados por nossa API.

[tjf]: https://tjf.totvs.com.br
[treports]: https://treports.totvs.com.br
[api]: https://api.totvs.com.br
[ttalk]: https://tdn.totvs.com/display/framework/T-TALK
[docker]: http://docker.com
[howtotreportsdocker]: https://www.youtube.com/watch?v=Al9NoY58DJs
[apicostcenter]: https://api.totvs.com.br/apidetails/CostCenter_v1_000.json
[treportsprovedorapi]: https://www.facebook.com/totvsdevelopers/videos/2355074714610562/?q=treports&epa=SEARCH_BOX
[totvsdevelopers]: https://www.facebook.com/totvsdevelopers
[howtotreports]: https://www.youtube.com/playlist?list=PLXa8l0dq5zRntVbTY2aORvbk2Bp1LdNnj
