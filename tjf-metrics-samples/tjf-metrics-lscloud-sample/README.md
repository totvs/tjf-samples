# TJF Metrics LSCloud Sample

_Sample_ de utilização da biblioteca [__TJF Metrics LSCloud__][tjf-metrics-lscloud] do [__TOTVS Java Framework__][tjf].

## Contexto

Para exemplificar o uso da biblioteca [__TJF Metrics LSCloud__][tjf-metrics-lscloud], criaremos uma aplicação simples, ela será responsável por publicar logs de uso para os canais de mensageria que serão lidos pelo adpter do **TOTVS LSCloud**, e também terá um subscriber que irá ler as mensagens de retorno caso ocorra um erro no processamento da mensagem pelo LSCloud.

## Começando

Iniciaremos o desenvolvimento criando um novo projeto [Spring][spring] que será o nosso publicador das mensagens, utilizando o serviço [Spring Initializr][spring-initializr].

## Configurações

Após gerado o projeto, precisamos substituir no arquivo `pom.xml` o _parent_ do projeto pela biblioteca [__TJF Boot Starter__][tjf-boot-starter]:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>5.11.0-RELEASE</version>
</parent>
```

Ela define algumas configuração para os projetos que utilizar os módulos do [__TOTVS Java Framework__][tjf].

Incluiremos também a dependência para utilização da biblioteca [__TJF Metrics LSCloud__][tjf-metrics-lscloud] e as configurações do repositório __Maven__ com a distribuição do [__TOTVS Java Framework__][tjf]:

_Dependências_

```xml
<dependencies>
	<!-- TJF -->
	<dependency>
		<groupId>com.totvs.tjf</groupId>
		<artifactId>tjf-metrics-lscloud</artifactId>
	</dependency>
</dependencies>
```

_Repositório_

```xml
<repositories>
	<repository>
		<id>tjf-release</id>
		<name>TOTVS Java Framework: Releases</name>
		<url>http://maven.engpro.totvs.com.br/artifactory/libs-release/</url>
	</repository>
</repositories>
```

Por fim, precisamos renomear o arquivo `application.properties`, da pasta `src/main/resources`, para `application.yml`, o qual editaremos mais a frente.

### Mecanismos de mensageria

No nosso exemplo usaremos a imagem do [RabbitMQ](https://hub.docker.com/_/rabbitmq/) como mecanismos de mensageria.
As configurações do Rabbit devem ser incluídas no arquivo `application.yml`:

```yaml
tjf:
  lscloud:
    module-id: "1234"
    group-id: "4567"
    slot-id: "7890"

spring:
  cloud:
    function:
      routing:
        enabled: true
      routing-expression: headers['type']
    stream:
      default-binder: rabbit1

      bindings:   
        lscloud-out-0:
          binder: rabbit1
          destination:  LogLicense.input

        functionRouter-in-0:
          binder: rabbit1
          destination: LogLicense.input.dlq

      binders:
        rabbit1:
          type: rabbit
      rabbit:
        default:
          exchange-type: headers

server:
  port: 8080
```

## Log de uso

Para iniciar, criaremos o pacote `com.tjf.sample.github.lscloud.controller` e iremos criar uma classe controller que irá enviar o log de uso criado pelo builder e usando o bean `Lscloud` fornecidos pelo TJF, essa classe tera dois endpoints, um responsavel por criar e enviar o log de uso e o outro irá forçar um erro para visualizarmos a mensagem de retorno:

_StarShipController.java_

```java
@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private Lscloud lscloud;

	public StarShipController(Lscloud lscloud) {
		this.lscloud = lscloud;
	}

	@GetMapping("/arrived")
	String starShipArrived(String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		var logLicense = LogLicense.builder().withRoutine("starshipArrived").build();
		lscloud.log(logLicense);

		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent to lscloud!";
	}

	@GetMapping("/error")
	String error() {

		var logLicense = LogLicense.builder().withRoutine("").build();
		lscloud.log(logLicense);

		return "View error on log!";
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
```
> O método `setTenant` é usado apenas para simplificar o uso de tenant, com o preenchimento automatico no log de uso.

## Tratamento de erros

Para o recebimento das mensagens de erro será criado um subscriber para receber as mensagens caso o erro ocorra, crie no pacote `com.tjf.sample.github.lscloud.error`:

_ErrorSubscriber.java_

```java
@Configuration
public class ErrorSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorSubscriber.class);

	@Bean(name = "com.totvs.tjf.lscloud.message.LogLicense")
    public Consumer<TOTVSMessage<?>> ErrorMessage() {
        return message -> {
            LOG.info("Lscloud error received:\nType: {}\nContent: {}", 
                    message.getHeader().getType(),
                    message.getContent());};
    }
}
```

## Vamos testar

No nosso exemplo você vai precisar estar com o `RabbitMQ` já configurado e os canais configurados no ambiente de dev to `TOTVS apps`.

Agora acesse a URL pelo navegador [http://<sample-em-dev>/starship/arrived?name=millenium%20falcon&tenant=031fba69-47df-4524-8577-ecd9377f3c2a](http://<sample-em-dev>/starship/arrived?name=millenium%20falcon&tenant=031fba69-47df-4524-8577-ecd9377f3c2a), nossa API rest criada irá publicar um log de uso para a mensageria, caso ocorra alguma inconsistencia na mensagem, vide o endpoint `error` que criamos, será recebida as mensagens pelo nosso subscriber e irá mostrar nos logs.

[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[tjf-boot-starter]: https://tjf.totvs.com.br/wiki/tjf-boot-starter
[RFC000011]: https://arquitetura.totvs.io/architectural-records/RFCs/Corporativas/RFC000011/
[tjf-metrics-lscloud]: https://tjf.totvs.com.br/wiki/tjf-metrics-lscloud
[tjf]: https://tjf.totvs.com.br
