# Messaging AMQP Sample

_Sample_ de utilização do módulo [__Messaging AMQP__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

Na implementação da aplicação que estiver usando o módulo tjf-messaging-amqp algumas configurações são necessárias. 


### Configuração dos parâmetros de conexão com o RabbitMQ

application.yml

```yml
tjf:
  messaging:
    amqp:
      hostname: localhost
      username: admin
      password: totvs@123
      port: 5672
```

No publisher deverá ser usada uma instância de RabbitTemplate para que esta seja passada para o método sendTo da classe AmqpTOTVSMessage

```java
@RequiredArgsConstructor
@Component
public class StarShipPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE = "starship-exchange-1";
    public static final String ROUTING_KEY = "starship-routing-key-1";

    public static final String EXCHANGE_2 = "starship-exchange-2";
    public static final String ROUTING_KEY_2 = "starship-routing-key-2";

    public static final String EXCHANGE_3 = "starship-exchange-3";
    public static final String ROUTING_KEY_3 = "starship-routing-key-3";

    public void publishEvent(StarShipArrivedEvent starShipArrivedEvent) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, starShipArrivedEvent);
    }

    public <T> void publishEvent(T event, String eventName) {
        TOTVSMessageBuilder.amqp().withType(eventName).setContent(event).buildAmqp()
                .sendTo(rabbitTemplate, EXCHANGE_2, ROUTING_KEY_2);
    }

    public <T> void publishEvent(T event, String eventName, TransactionInfo transactionInfo, CloudEventsInfo cloudEventsInfo) {
        TOTVSMessageBuilder.amqp().withType(eventName).setContent(event)
                .setTransactionInfo(transactionInfo)
                .setCloudEventsInfo(cloudEventsInfo)
                .buildAmqp()
                .sendTo(rabbitTemplate, EXCHANGE_3, ROUTING_KEY_3);
    }
}
```

### Consumindo mensagens

As mesmas configurações de parâmetros de conexão com o RabbitMQ feitas no publisher também são necessárias na aplicação que irá consumir as mensagens.

application.yml
```yml
tjf:
  messaging:
    amqp:
      hostname: localhost
      username: admin
      password: totvs@123
      port: 5672
```

E os métodos responsáveis por receberem as mensagens precisam estar anotados com @RabbitListener.

```java
@Slf4j
@RequiredArgsConstructor
@Component
public class StarShipSubscriber {

    private final ObjectMapper objectMapper;

    public static final String QUEUE_1 = "starship-queue-1";
    public static final String QUEUE_2 = "starship-queue-2";
    public static final String QUEUE_3 = "starship-queue-3";

    @RabbitListener(queues = QUEUE_1)
    public void receive(@Payload StarShipArrivedEvent starShipArrivedEvent) {
        log.info("Received message from queue: {}", QUEUE_1);
        log.info("StarShipArrivedEvent with name {}", starShipArrivedEvent.getName());
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_2)
    public void receiveTotvsMessage(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_2, objectMapper.writeValueAsString(message));
        log.info(objectMapper.writeValueAsString(message));
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_3)
    public void receiveTotvsMessageAsCloudEvent(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_3, objectMapper.writeValueAsString(message));
    }
}
```

## Vamos testar

No nosso exemplo você vai precisar estar com o `RabbitMQ` já configurado e em execução, por conversão como não especificamos as portas ele irá usar as portas padrões.
As seguintes os exchanges precisam estar criados também. Sendo eles: starship-exchange-1, starship-exchange-2 e starship-exchange-3.
Assim como as filas: starship-queue-1, starship-queue-2 e starship-queue-3.
E os bindings devem ser feitos utilizando routings key. Sendo eles:

- starship-exchange-1 / starship-queue-1: routing key: starship-routing-key-1
- starship-exchange-2 / starship-queue-2: routing key: starship-routing-key-2
- starship-exchange-3 / starship-queue-3: routing key: starship-routing-key-3
 

Execute os nossos dois projetos.

Agora acesse a URL pelo navegador 
 - http://localhost:8082/starship/arrived?name=millenium
 - http://localhost:8082/starship/arrived-tm?name=millenium
 - http://localhost:8082/starship/arrived-tm-ce?name=millenium

> Para mais informações sobre a definição dos campos, veja a documentação do padrão de CloudEvents no Modelo base de mensagem para comunicação entre serviços da TOTVS na [RFC000011].

[tjf-messaging-stream]: https://tjf.totvs.com.br/wiki/v3/tjf-messaging-stream
[tjf]: https://tjf.totvs.com.br
[RFC000011]: https://arquitetura.totvs.io/architectural-records/RFCs/Corporativas/RFC000011/
