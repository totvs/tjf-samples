# Messaging Stream Function Sample

_Sample_ de utilização de _functions_ com a biblioteca [__Messaging Stream__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

Na implementação de _functions_ utilizando o tjf-messaging-stream, serão necessários alguns ajustes nos códigos do *publisher* para retirar a anotação `@EnableBinding` e a utilização das *exchanges* que serão descontinuadas. Para o *subscribers* também teremos algumas alterações já que a anotação `@StreamListener` foi depreciada nessa versão do Spring Cloud Stream. 

No exemplo publicado, tentaremos simplificar ao máximo a utilização após a migração.

### Alterações no *sample publisher*

Nova configuração da aplicação:

```yml
spring:
  cloud:
    stream:
      defaultBinder: rabbit1

      binders:
        rabbit1:
          type: rabbit
          environment:
            spring:
              rabbit:
                host: localhost
      
      bindings:   
        publishArrived-out-0:
          binder: rabbit1
          destination: starship-input
          group: writers
        publishLeft-out-0:
          binder: rabbit1
          destination: starship-input
          group: writers

      rabbit:
        default:
          exchangeType: headers
        bindings:
          publishArrived-out-0:
            producer:
              exchangeType: headers
          publishLeft-out-0:
            producer:
              exchangeType: headers
```

Novo *publisher* sem a utilização do `@EnableBinding` e utilizando o `StreamBridge` para publicação nos *bindings* definidos anteriormente:

```java
@Component
public class StarShipPublisher {

	@Autowired
	private StreamBridge streamBridge;

	public void publishArrivedEvent(StarShipArrivedEvent starShipEvent) {
		System.out.println(starShipEvent.getClass().getSimpleName() + " enviado!");
		
		var message = MessageBuilder.withPayload(new TOTVSMessage<>(StarShipArrivedEvent.NAME, starShipEvent))
				.setHeader("type", "StarShipArrivedEvent").build();

		streamBridge.send("publishArrived-out-0", message);
	}

	public void publishLeftEvent(StarShipLeftEvent starShipEvent) {
		System.out.println(starShipEvent.getClass().getSimpleName() + " enviado!");

		var message = MessageBuilder.withPayload(new TOTVSMessage<>(StarShipLeftEvent.NAME, starShipEvent))
				.setHeader("type", "StarShipLeftEvent").build();

		streamBridge.send("publishLeft-out-0", message);
	}
}
```

### Alterações no *sample subscriber*

Primeiramente incluímos as propriedades `spring.cloud.function.routing.enabled=true` e `spring.cloud.function.routing-expression=headers['type']`. Com isso, habilitamos o filtro e adicionamos o *header* `type` como filtro para os *beans*.

```yml
spring:
  cloud:
    function:
      routing:
       	enabled: true
      routing-expression: headers['type']
```

Criaremos um *binding* genérico para recebimento das mensagens, conforme exemplo:

```yml
functionRouter-in-0:
  binder: rabbit1
  destination: starship-input
  group: request
```
> O nome _functionRouter_ é fixo, conforme documentação do String.

> OBS: Além disso, poderá ser criado um *binding* de entrada (in) e de saída (out) para cada tipo de mensagem:

```yml
StarShipArrivedEvent-in-0:
  binder: rabbit1
  destination: starship-input
  group: request
StarShipArrivedEvent-out-0:
  binder: rabbit1
  destination: starship-input
  group: request 
StarShipLeftEvent-in-0:
  binder: rabbit1
  destination: starship-input
  group: request 
StarShipLeftEvent-out-0:
  binder: rabbit1
  destination: starship-input
  group: request
```
      
Agora, basta criar um @Bean para cada *type* que será recebido, conforme exemplos:
      
```java
@Bean
public Consumer<TOTVSMessage<StarShipLeftEvent>> StarShipLeftEvent() {
	return message -> {
		System.out.println("StarShipLeftEvent recebido!");

		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	};
}

@Bean
public Consumer<TOTVSMessage<StarShipArrivedEvent>> StarShipArrivedEvent() {
	return message -> {
		System.out.println("StarShipArrivedEvent recebido!");

		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

		if (transactionContext.getTransactionInfo() != null) {
			System.out.println(
					"TransactionInfo TransactionId: " + transactionContext.getTransactionInfo().getTransactionId());
			System.out.println(
					"TransactionInfo GeneratedBy: " + transactionContext.getTransactionInfo().getGeneratedBy());
		}

		if (message.getHeader().getCloudEventsInfo() != null) {
			System.out.println("CloudEventInfo Id: " + message.getHeader().getCloudEventsInfo().getId());
			System.out
					.println("CloudEventInfo Schema: " + message.getHeader().getCloudEventsInfo().getDataSchema());
			System.out.println("CloudEventInfo DataContentType: "
					+ message.getHeader().getCloudEventsInfo().getDataContentType());
		}

	};
}	
```

> OBS: O *routing* é feito pelo nome do *Bean*. Como por padrão os métodos iniciam com letras minúsculas, temos algumas opções: manter os tipos com a inicial minúscula, criar somente um @Bean que receba todas as mensagens e faça o redirecionamento ou filtrar no próprio *routing-expression*.

```yml
routing-expression: "headers['type']=='StarShipArrivedEvent' ? 'arrivedEvent' : 'leftEvent'"
```

### Filas de erro

Assim como comunicado no portal do TJF, as filas de erros também passaram por mudanças, segue o novo padrão:

```yml
tjf-messaging-error-in-0:
  destination: bbunit-errors
  group: errors

tjf-messaging-error-out-0:
  destination: bbunit-errors
  group: errors
  producer:
    requiredGroups: errors
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

Você também pode usar o _endpoint_ que criamos para envio de CloudEvents, pro exemplo http://localhost:8080/starship/arrived/cloudevent?name=nave1&tenant=2 você verá algo como abaixo no console do app `subscriber`:

```console
TransactionInfo TaskId: f2d4bb79-448f-425a-906d-060e227c9c21
Current tenant: 2
CloudEventInfo Id: acd12cac-73a7-4c85-88ed-971881e8c9c2
CloudEventInfo Schema: 2

StarShip arrived!
Starship name: nave1
```

> Para mais informações sobre a definição dos campos, veja a documentação do padrão de CloudEvents no Modelo base de mensagem para comunicação entre serviços da TOTVS na [RFC000011].

[tjf-messaging-stream]: https://tjf.totvs.com.br/wiki/v3/tjf-messaging-stream
[tjf]: https://tjf.totvs.com.br
[RFC000011]: https://arquitetura.totvs.io/architectural-records/RFCs/Corporativas/RFC000011/
