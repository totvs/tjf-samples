# Messaging Stream Function Sample

_Sample_ de utilização de _functions_ com a biblioteca [__Messaging Stream__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

Na implementação de _functions_ utilizando o tjf-messaging-stream, não é necessário alterar os códigos do *publishers*, somente as configurações da aplicação. Já para o *subscribers* teremos algumas mudanças, já que a anotação `@StreamListener` foi descontinuada nessa versão do Spring Cloud Stream. 

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
        starship-input:
          destination: starship-input
          group: writers
          binder: rabbit1

      rabbit:
        default:
          exchangeType: headers
        bindings:
          starship-input:
            producer:
              exchangeType: headers
```

### Alterações no *sample subscriber*

Primeiramente incluímos as propriedades spring.cloud.function.routing.enabled=true e spring.cloud.function.routing-expression=headers['type']. Com isso, habilitamos o filtro e adicionamos o *header* `type` como filtro para os *beans*.

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

> OBS: O *routing* é feito pelo nome do Bean, como por padrão os métodos iniciam com letras minúsculas, temos algumas opções: manter os tipos com a inicial minúscula, criar somente um @Bean que receba todas as mensagens e faça o redirecionamento ou filtrar no próprio *routing-expression*.

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

...
