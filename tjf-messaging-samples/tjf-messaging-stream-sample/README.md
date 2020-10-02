# Messaging Stream Sample

_Sample_ de utilização da biblioteca [__Messaging Stream__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

Para exemplificar o uso da biblioteca [__Messaging Stream__][tjf-messaging-stream], criaremos duas aplicações, uma será responsável por publicar a mensagem na mensageria, o Publisher, e a outra por receber a mensagem da mensageria, o Subscriber.

## Começando

Iniciaremos o desenvolvimento criando um novo projeto [Spring][spring] que será o nosso publicador das mensagens, utilizando o serviço [Spring Initializr][spring-initializr].

Após informados os dados e incluídas as dependências necessárias, podemos iniciar a geração do projeto.

Agora criaremos um novo projeto também pelo [Spring Initializr][spring-initializr], esse será o nosso receptor das mensagens.

## Configurações

Após gerado os dois projetos, precisamos substituir no arquivo `pom.xml` de ambos o _parent_ do projeto pela biblioteca [__TJF Boot Starter__][tjf-boot-starter]:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
</parent>
```

Ela define algumas configuração para os projetos que utilizar os módulos do [__TOTVS Java Framework__][tjf].

Incluiremos também a dependência para utilização da biblioteca [__Messaging Stream__][tjf-messaging-stream] e as configurações do repositório __Maven__ com a distribuição do [__TOTVS Java Framework__][tjf]:

_Dependências_

```xml
<dependencies>
	<!-- TJF -->
	<dependency>
	<groupId>com.totvs.tjf</groupId>
	<artifactId>tjf-messaging-stream</artifactId>
	</dependency>

	<!-- Lombok -->
	<dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
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

> No nosso arquivo `application.yml` temos também configurações para rodar os _samples_ utilizando o `Kafka` e o `ActiveMQ`, basta descomentar a opção desejada. Disponibilizamos três arquivos `docker-compose`, um para cada imagem.

### Entidades

Para iniciar, criaremos o pacote `com.tjf.sample.github.messaging.model`, para guardar a classe do nosso modelo de dados, e dentro dele criaremos a classe que representa a `starship`:

_StarShip.java_

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarShip {
	private String name;
}
```

### Eventos

Criaremos duas classes no pacote `com.tjf.sample.github.messaging.events`, uma para o evento da nave chegar no gate e outra para quando uma nave sai do gate, respectivamente:

_StarShipArrivedEvent.java_

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarShipArrivedEvent {
	public static final transient String NAME = "StarShipArrivedEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

	private String name;
}
```

_StarShipLeftEvent.java_

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarShipLeftEvent {
	public static final transient String NAME = "StarShipLeftEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

	@NotBlank
	private String name;
}
```

## API REST

### Controller

Iremos criar uma _API REST_ no nosso projeto de publicação, apenas como forma de receber os dados que usaremos para testar e transformar na mensagens do nosso exemplo.

Criaremos o pacote `com.tjf.sample.github.messaging.controller` para guardar a classe que irá receber os dados via rest e irá criar e chamar o método para publicar nossa mensagem, para demostrarmos o uso do multi tenant criaremos o metodo setTenant, ele irá alterar o tenant atual do contexto, assim automaticamente as mensagem enviadas pra fila passam a ser do tenant recebido via rest.

_StarShipController.java_

```java
@RestController
@RequestMapping(path = "/starship")
public class StarShipController {
	
	private StarShipPublisher samplePublisher;
	
	public StarShipController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}
	
	@GetMapping("/arrived")
    String starShipArrived(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {
        
		this.setTenant(tenant);
        
		System.out.println("\nStarship arrived name: " + name);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);
        samplePublisher.publish(starShipEvent, StarShipArrivedEvent.NAME);
        
        return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!";
    }
	
	@GetMapping("/left")
    String starShipLeft(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {
        
		this.setTenant(tenant);
        
		System.out.println("\nStarship left name: " + name);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShipLeftEvent starShipEvent = new StarShipLeftEvent(name);
        samplePublisher.publish(starShipEvent, StarShipLeftEvent.NAME);
        
        return "The identification of the left starship " + name + " of tenant " + tenant + " was sent!";
    }

	@GetMapping("/arrived/transacted")
	String starShipArrivedTransacted(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship arrived name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);

		String id = UUID.randomUUID().toString();
		TransactionInfo transaction = new TransactionInfo(id, starShipEvent.toString());
		transactions.put(id, Status.SENDED);

		samplePublisher.publish(starShipEvent, StarShipArrivedEvent.NAME, transaction);

		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!\n"
				+ "In transaction " + id + ", acess http://localhost:8080/starship/transaction/" + id
				+ " to consult the status.";
	}
	
	@GetMapping("/transaction/{id}")
	String starShipArrivedTransacted(@PathVariable("id") String id) {

		Status status = transactions.get(id);

		return status != null ? "Status: " + status.toString() : "Transaction " + id + " not found!";
	}

	@PostMapping("/transaction")
	void closeTransaction(@RequestBody TransactionInfo transaction) {
		transactions.replace(transaction.getTransactionId(), Status.CONCLUDED);
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A",
				null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	enum Status {
		SENDED, CONCLUDED;
	}
}
```

## Transações

O _endpoint_ `starship/arrived/transacted` faz o mesmo que o `starship/arrived`, mas para exemplificar o uso de transações ele cria no header da mensagem o `transactionInfo`, o projeto subscriber lê essas informações setadas automaticamente do contexto, ele imprime no console e para simplificar os projetos de exemplo o subscriber responde via rest que recebeu a transação, pode ser consultado o status da transação no _endpoint_ `/starship/transaction/<id>`.

## Infrastructure

### Exchange

Iremos agora criar a interface que irá definir os canais nos quais faremos a comunicação das mensagens, será criada em ambos os projetos.

Criaremos o pacote `com.tjf.sample.github.messaging.infrastructure.messaging` para guardar a classe que defini os canais.

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

### publisher

No projeto de publicação criaremos a classe responsável por publicar a mensagem, crie no pacote `com.tjf.sample.github.messaging.infrastructure.messaging`.

_StarShipPublisher.java_

```java
@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {

		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}
}
```

### Subscriber

No projeto que será inscrito para receber as mensagens criaremos a classe responsável por receber a mensagem, crie no pacote `com.tjf.sample.github.messaging.infrastructure.messaging`.

_StarShipSubscriber.java_

```java
@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	private StarShipService starShipService;

	@Autowired
	private TransactionContext transactionContext;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
	}

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {

		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

		System.out.println("TransactionInfo TransactionId: "
				+ transactionContext.getTransactionInfo().getTransactionId());
		System.out.println("TransactionInfo GeneratedBy: "
				+ transactionContext.getTransactionInfo());
	}

	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {
		
		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}
}
```

Criaremos também no projeto receptor a classe que irá dar uma mensagem no log ao receber a mensagem, crie-a no pacote  `com.tjf.sample.github.messaging.services`, nele também tem um contador simples, mas que é separado pelo _tenant_ corrente.

_StarShipService.java_

```java
public class StarShipService {

	private final HashMap<String, String> starShips = new HashMap<>();
	private final HashMap<String, Integer> counter = new HashMap<>();
	
	public StarShipService() {
		starShips.put("millenium falcon", "1");
		starShips.put("star destroyer", "2");
		starShips.put("j-type 327", "3");
		starShips.put("at", "4");
		starShips.put("snowspeeder", "5");
		starShips.put("tie figther", "6");
		starShips.put("naboo starfigther", "7");
		starShips.put("b-wing", "8");
		starShips.put("speeder bike", "9");
		starShips.put("x-wing", "10");
	}
	
	public void arrived(StarShip starShip) {
		
		String rank = starShips.getOrDefault(starShip.getName(), "Unknown starship!");
				
		System.out.println("\nStarShip arrived!\n");
		System.out.println("Current tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + rank);
		System.out.println("Counter by tenant: " + arrivedCount());
	}
	
	public void left(StarShip starShip) {
		
		String rank = starShips.getOrDefault(starShip.getName(), "Unknown starship!");
		
		System.out.println("\nStarShip left!\n");
		System.out.println("Current tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + rank);
		System.out.println("Counter by tenant: " + leftCount());
	}
	
	private int arrivedCount() {
		
		String tenant = SecurityDetails.getTenant();
		counter.put(tenant, counter.getOrDefault(tenant, 0) + 1);
		
		return counter.get(tenant); 
	}
	
	private int leftCount() {
		
		String tenant = SecurityDetails.getTenant();
		counter.put(tenant, counter.getOrDefault(tenant, 0) - 1);
		
		return counter.get(tenant); 
	}

	public void transactionClose(TransactionInfo transaction) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map map = new ObjectMapper().convertValue(transaction, Map.class);

		HttpEntity<Map> request = new HttpEntity<Map>(map, headers);

		rest.postForEntity("http://localhost:8080/starship/transaction", request, String.class);
	}
}

```

## Vamos testar

No nosso exemplo você vai precisar estar com o `RabbitMQ` já configurado e em execução, por conversão como não especificamos as portas ele irá usar as portas padrões.
Execute os nossos dois projetos.

Agora acesse a URL pelo navegador [http://localhost:8080/starship/arrived?name=millenium%20falcon&tenant=Alderaan](http://localhost:8080/starship/arrived?name=millenium%20falcon&tenant=Alderaan), nossa API rest criada irá publicar para a mensageria uma mensagem de evento e nosso outro projeto deve receber a mensagem e mostrar no log, perceba que o tenant também foi carregado com sucesso:

```
StarShip arrived!

Current tenant: Alderaan
Starship name: millenium falcon
Starship ranking: 1
Counter by tenant: 1
```

Agora vamos enviar uma nave saindo do gate [http://localhost:8080/starship/left?name=at&tenant=Alderaan](http://localhost:8080/starship/left?name=at&tenant=Alderaan):

```
StarShip left!

Current tenant: Alderaan
Starship name: at
Starship ranking: 4
Counter by tenant: 0
```

## Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__Messaging Stream__][tjf-messaging-stream] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf-messaging-stream]: https://tjf.totvs.com.br/wiki/tjf-messaging-stream
[tjf]: https://tjf.totvs.com.br
[spring]: https://spring.io
[spring-initializr]: https://start.spring.io
[tjf-boot-starter]: https://tjf.totvs.com.br/wiki/tjf-boot-starter
