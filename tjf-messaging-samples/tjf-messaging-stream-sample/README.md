# Messaging Stream Sample

_Sample_ de utilização da biblioteca [__Messaging Stream__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

Para exemplificar o uso da biblioteca [__Messaging Stream__][tjf-messaging-stream], criaremos duas aplicações, uma será responsável por publicar a mensagem na mensageria, o Publisher, e a outra por receber a mensagem da mensageria, o Subscriber.


# Começando

Iniciaremos o desenvolvimento criando um novo projeto [Spring][spring] que será o nosso publicador das mensagens, utilizando o serviço [Spring Initializr][spring-initializr].

Após informados os dados e incluídas as dependências necessárias, podemos iniciar a geração do projeto.

Agora criaremos um novo projeto também pelo [Spring Initializr][spring-initializr], esse será o nosso receptor das mensagens.

## Configurações

Após gerado os dois projetos, precisamos substituir no arquivo `pom.xml` de ambos o _parent_ do projeto pela biblioteca [__TJF Boot Starter__][tjf-boot-starter]:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>0.2.0-RELEASE</version>
</parent>
```
Ela define algumas configuração para os projetos que utilizar os módulos do [__TOTVS Java Framework__][tjf].

Incluiremos também a dependência para utilização da biblioteca [__Messaging Stream__][tjf-messaging-stream] e as configurações do repositório __Maven__ com a distribuição do [__TOTVS Java Framework__][tjf]:

_Dependências_

```xml
<dependencies>
  ...

  <!-- TJF -->
  <dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-messaging-stream</artifactId>
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

Por fim, precisamos renomear o arquivo `application.properties`, da pasta `src/main/resources`, para `application.yml`, o qual editaremos mais a frente.


### Mecanismos de mensageria

No nosso exemplo usaremos a imagem do [RabbitMQ](https://hub.docker.com/_/rabbitmq/) como mecanismos de mensageria, existem outros compatíveis conforme documentação do [__Messaging Stream__][tjf-messaging-stream].
As configurações do Rabbit devem ser incluídas no arquivo `application.yml` para ambos os projetos mudando apenas o server port em cada um deles:

```yaml
spring:
  cloud:
    stream:
      defaultBinder: rabbit1
      bindings:
        starship-input:
          destination: starship-input
          group: requests
        starship-output:
          destination: starship-input
      binders:
        rabbit1:
          type: rabbit
          environment:
            spring:
              rabbit:
                host: localhost
server:
  port: <8080 no Publisher | 8180 no Subscriber>

```

### Entidades

Para iniciar, criaremos o pacote `br.com.star.wars.messaging.model`, para guardar a classe do nosso modelo de dados, e dentro dele criaremos a classe que representa a `starship`, como usaremos como exemplo multi tenant implementaremos em nossa classe a interface `com.totvs.tjf.messaging.Tenantable`:

_StarShip.java_

```java
public class StarShip implements Tenantable {

	private String name;
	private String tenantId;

	public StarShip(String name) {
		this.name = name;
	}

	public StarShip() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	@Override
	public String getTenantId() {
		return this.tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
```

# API REST

## Controller

Iremos criar uma _API REST_ no nosso projeto de publicação, apenas para receber os dados que usaremos e transformar na mensagens do nosso exemplo.

Criaremos o pacote `br.com.star.wars.messaging.controller` para guardar a classe que irá receber os dados via rest e irá criar e chamar o método para publicar nossa mensagem, para demostrarmos o multi tenant criaremos o metodo setTenant, ele irá alterar o tenant atual para que as mensagem enviadas passem a ser do tenant enviado via rest.

_StarShipController.java_

```java
@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private StarShipPublisher samplePublisher;

	public StarShipController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}

	@GetMapping
    String starShip(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {

		System.out.println("\nStarship name: " + name);

		this.setTenant(tenant);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShip starShip = new StarShip(name);
    	samplePublisher.publish(starShip);

        return "The identification of the starship " + name + " of tenant " + tenant + " was sent!";
    }

	private void setTenant(String tenant) {

		SecurityPrincipal principal = new SecurityPrincipal("", tenant, tenant.split("-")[0]);
	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
```

# Infrastructure

## Exchange

Iremos agora criar a interface que irá definir os canais nos quais faremos a comunicação das mensagens, será criada em ambos os projetos.

Criaremos o pacote `br.com.star.wars.messaging.infrastructure.messaging` para guardar a classe que defini os canais.

Publisher _StarShipExchange.java_

```java
public interface StarShipExchange {

	String OUTPUT = "starship-output";

	@Output(StarShipExchange.OUTPUT)
	MessageChannel output();
}
```

Subscriber _StarShipExchange.java_

```java
public interface StarShipExchange {

	String INPUT = "starship-input";

	@Input(StarShipExchange.INPUT)
	SubscribableChannel input();
}
```

## Publisher

No projeto de publicação criaremos a classe responsável por publicar a mensagem, crie no pacote `br.com.star.wars.messaging.infrastructure.messaging`.

_StarShipPublisher.java_

```java
@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	@StreamPublisher
	public void publish(StarShip starShip) {
		exchange.output().send(MessageBuilder.withPayload(starShip).setHeader("command", "arrivedStarShip").build());
	}
}
```

## Subscriber

No projeto que será inscrito para receber as mensagens criaremos a classe responsável por receber a mensagem, crie no pacote `br.com.star.wars.messaging.infrastructure.messaging`.

_StarShipSubscriber.java_

```java
@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	private StarShipService starShipService;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
	}

	@StreamListener(target = StarShipExchange.INPUT, condition = "headers['command']=='arrivedStarShip'")
	public void subscribe(StarShip starShip) {
		starShipService.arrived(starShip);
	}
}
```
Criaremos também no projeto receptor a classe que irar dar uma mensagem no log ao receber a mensagem, crie-a no pacote  `br.com.star.wars.messaging.services`.

_StarShipService.java_

```java
public class StarShipService {

	private final HashMap<String, Integer> starShips = new HashMap<String, Integer>();

	public StarShipService() {
		starShips.put("millenium falcon", 1);
		starShips.put("star destroyer", 2);
		starShips.put("j-type 327", 3);
		starShips.put("at", 4);
		starShips.put("snowspeeder", 5);
		starShips.put("tie figther", 6);
		starShips.put("naboo starfigther", 7);
		starShips.put("b-wing", 8);
		starShips.put("speeder bike", 9);
		starShips.put("x-wing", 10);
	}

	public void arrived(StarShip starShip) {

		int rank = starShips.getOrDefault(starShip.getName().toLowerCase(), 0);

		System.out.println("\nCurrent Tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + (rank == 0 ? "Unknown" : rank));
	}
}
```

# Vamos testar

No nosso exemplo você vai precisar estar com o `RabbitMQ` já configurado e em execução.
Execute o nossos dois projetos.

Agora acesse a URL pelo navegador [http://localhost:8080/starship?name=Millenium%20Falcon&tenant=Alderaan](http://localhost:8080/starship?name=Millenium%20Falcon&tenant=Alderaan), nossa API rest criada irá publicar para a mensageria uma mensagem e nosso outro projeto deve receber a mensagem e mostrar no log, perceba que o tenant também foi carregado com sucesso:

```
Current Tenant: Alderaan
Starship name: Millenium Falcon
Starship ranking: 1
```


# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__Messaging Stream__][tjf-messaging-stream] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf-messaging-stream]: https://tjf.totvs.com.br/wikiV020/tjf-messaging-stream
[tjf]: https://tjf.totvs.com.br
[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[tjf-boot-starter]: https://tjf.totvs.com.br/wikiV020/tjf-boot-starter