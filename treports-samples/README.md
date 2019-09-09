# TReports Sample

_Sample_ da utilização do [__TReports__][treports] com aplicações contruidas usando o [__TOTVS Java Framework__][tjf].

## Contexto

Uma das maneiras de integrar o [TReports][treports] à aplicação é fazendo acesso às API, para isso as API devem estar cadastradas no [API reference TOTVS][api].

> **Observação:** mais informações de como construir as API e como disponibiliza-las no portal pode ser encontrada na [página do TTalk no TDN][ttalk].

# Começando



Usaremos o [Docker][docker] para rodar o TReports, segue abaixo o docker-compose.yml com as definições necessárias para rodar as imagens do RAC, do TReports e do banco de dados que precisáremos para execução da nossa instancia do TReports.

```yml
version: '3.6'

networks:
  docker-network-trep:
    driver: bridge

services:
  treports:
    image: docker.totvs.io/treports/treports:12125
    build:
      context: .
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
      DOCKER_DEFAULTTENANT: "treports"
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

      Tenants__0__TenantName: "treports"
      Tenants__0__Name: "TReports"
      Tenants__0__CNPJ: "13030973000115"
      Tenants__0__Products__0__ProductName: "TOTVS RAC"
      Tenants__0__Products__1__ProductName: "TReports"
      HostUrl: "http://*.localhost:5009/totvs.rac"
    ports:
     - "5009:80"
    networks:
     - docker-network-trep
    depends_on:
     - db

  db:
    image: mcr.microsoft.com/mssql/server:2017-CU11-ubuntu
    environment:
      SA_PASSWORD: "012345678@totvs123"
      ACCEPT_EULA: "Y"
    ports:
     - "14336:1433"
    networks:
     - docker-network-trep
```

Para subir o ambiente do TReports use o comando abaixo:

```shell
$ docker-compose up -d -f docker-compose.yml
```

> **Observação:** Caso queira mais informações e dicas de como sumir no docker o TReports e os serviços que ele nescessita, você pode encontrar no [canal de How To da TOTVS][howtotreportsdocker] no Youtube.

Pricisamos configurar o RAC antes de continuar, acesse o RAC pela URL http://localhost:5009/, tenant `treports` usuário `admin` e senha `totvs@123`. Crie um novo perfil e de permissão a ele nas features do TReports, depois altere o usuário e acrecente a ele o perfil que foi criamos.

No nosso exemplo iremos utilizar algumas definições de API prontas de outras aplicações.

[tjf]: https://tjf.totvs.com.br
[treports]: https://treports.totvs.com.br
[api]: https://api.totvs.com.br
[ttalk]: https://tdn.totvs.com/display/framework/T-TALK
[docker]: http://docker.com
[howtotreportsdocker]: https://www.youtube.com/watch?v=Al9NoY58DJs&feature=youtu.be