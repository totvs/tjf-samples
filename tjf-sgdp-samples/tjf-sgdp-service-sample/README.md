# SGDP - Sistema de Gestão de Dados Pessoais

# Contexto

Para exemplificar a biblioteca **SGDP** vamos utilizar o mesmo projeto criado para o _sample_ da biblioteca **API JPA** disponível em nosso [GitHub][tjf-api-jpa-sample].

# Começando com SGDP

Com o projeto _sample_ **API JPA** em mãos, vamos alterar alguns campos no banco de dados e incluir as anotações disponibilizadas pelo SGDP na classe de entidade.

> Como _engine_ de banco de dados utilizaremos o [H2][h2].

## Dependências

Incluir a biblioteca como dependência no `pom.xml` da aplicação:

```xml
<dependency>
	<groupId>com.totvs.sgdp.sdk</groupId>
	<artifactId>sgdp</artifactId>
	<version>{versão}</version>
</dependency>
```

Além das propriedades existentes no `application.yml` da aplicação, será necessário incluir novas propriedades. Conforme o exemplo:

```yml
  cloud:
    stream:

      kafka:
        binder:
          brokers: localhost:9092
          configuration:
            auto:
              offset:
                reset: earliest       

      binders:
        kafka1:
          type: kafka
        rabbit1:
          type: rabbit
          environment:
            spring:
              habbit:
                host: localhost:5672

      bindings:
      
        sgdp-kafka-reader:
          destination: sgdp-audit
          binder: kafka1
        sgdp-audit:
          destination: sgdp-audit
          contentType: application/json
          binder: kafka1
            
        sgdp-input:
          destination: sgdp-responses
          group: sw-sgdp-service
          binder: rabbit1
        sgdp-output:
          destination: sgdp-commands
          binder: rabbit1          
```

OBS: Para executar localmente, devemos alterar o *broker* para `localhost:9092`. 

## Auditoria

Para realizar a auditoria das informações, precisaremos subir uma imagem do Kafka no docker e para isso temos um `docker-compose.yml` na raiz desse projeto.

docker-compose.yml:

```yml
version: '2'

services:

    zoo1:
        image: zookeeper:3.4.9
        hostname: zoo1
        ports:
          - "2181:2181"
        environment:
            ZOO_MY_ID: 1
            ZOO_PORT: 2181
            ZOO_SERVERS: server.1=zoo1:2888:3888

    kafka1:
        image: confluentinc/cp-kafka:5.3.1
        hostname: kafka1
        ports:
          - "9092:9092"
        environment:
          KAFKA_ADVERTISED_LISTENERS: LISTENER_DOCKER_INTERNAL://kafka1:19092,LISTENER_DOCKER_EXTERNAL://127.0.0.1:9092
          KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: LISTENER_DOCKER_INTERNAL:PLAINTEXT,LISTENER_DOCKER_EXTERNAL:PLAINTEXT
          KAFKA_INTER_BROKER_LISTENER_NAME: LISTENER_DOCKER_INTERNAL
          KAFKA_ZOOKEEPER_CONNECT: "zoo1:2181"
          KAFKA_BROKER_ID: 1
          KAFKA_LOG4J_LOGGERS: "kafka.controller=INFO,kafka.producer.async.DefaultEventHandler=INFO,state.change.logger=INFO"
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
        depends_on:
          - zoo1
```

Apenas para validação, implementaremos duas classes para verificar o envio das mensagens para o Kafka.

**SGDPKafkaReader.java** - Utilizada como exchange para definir a fila de leitura.

```java
package br.com.star.wars;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SGDPKafkaReader {
	
	String INPUT = "sgdp-kafka-reader";

	@Input(SGDPKafkaReader.INPUT)
	SubscribableChannel input();
    
}
```

**Subscriber.java** - Servirá como _listener_ para interceptarmos as mensagens que são enviadas para o Kafka.

```java
package br.com.star.wars;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;

@Async
@EnableBinding(SGDPKafkaReader.class)
public class Subscriber {
	
	@StreamListener(target = SGDPKafkaReader.INPUT)
	public void listener(Message<?> message) {
		System.out.println("#################### MENSAGEM ENVIADA ######################");
		System.out.println(message);
	}

}
```

# Hora de testar

> Os endpoints podem ser testados também via **Swagger-UI** pela url `localhost:9190/swagger-ui.html`.

**Registro de tratamento de dados pessoais**

Ao efetuar um GET no endpoint `localhost:8180/jedi/v1/jedis` da aplicação **sw-sgdp-app**, teremos as seguintes mensagens no log do serviço **sw-sgdp-service**.

```
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"0b7a2db3-ddff-415f-acfd-463fcfafa0c7","tenantId":null,"timestamp":"2019-12-18T14:52:31.745827100","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Qui-Gon Jinn","identification":7777,"email":"jinn@space.com","gender":"male"}}, headers={kafka_offset=24, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551785}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"f51703d7-b454-4578-b63f-099aa2c2e91f","tenantId":null,"timestamp":"2019-12-18T14:52:31.794857500","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Obi-Wan Kenobi","identification":123,"email":"obi@jedi.com","gender":"male"}}, headers={kafka_offset=25, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551794}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"18a8fbd9-e59a-4707-9c0d-0a4caf0da9d7","tenantId":null,"timestamp":"2019-12-18T14:52:31.795792200","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Anakin Skywalker","identification":6666,"email":"darkin@space.com","gender":"male"}}, headers={kafka_offset=26, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551795}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"f71cc272-35c3-40f8-97ab-de2abe09ea9f","tenantId":null,"timestamp":"2019-12-18T14:52:31.795792200","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Yoda","identification":1,"email":"yoda@space.com","gender":"male"}}, headers={kafka_offset=27, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551796}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"99076b32-d692-454f-96fc-c0fa9863d890","tenantId":null,"timestamp":"2019-12-18T14:52:31.796795200","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Mace Windu","identification":2341,"email":"mace_windu@jedi.com","gender":"male"}}, headers={kafka_offset=28, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551796}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"ecc35ce0-0ec5-4d26-b9d0-15de32f3c53b","tenantId":null,"timestamp":"2019-12-18T14:52:31.796795200","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Count Dooku","identification":2431,"email":"dooku@dark.com","gender":"male"}}, headers={kafka_offset=29, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551797}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"579be82c-497f-4b15-a22b-cdaee7c2c06c","tenantId":null,"timestamp":"2019-12-18T14:52:31.797793","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Luke Skywalker","identification":8543,"email":"luke@jedi.com","gender":"male"}}, headers={kafka_offset=30, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551797}]
#################### MENSAGEM ENVIADA ######################
GenericMessage [payload={"uuid":"23294bc3-8c9e-43f8-8dbe-3ffc14a5b5a3","tenantId":null,"timestamp":"2019-12-18T14:52:31.797793","user":"anonymousUser","code":"","table":"br.com.star.wars.model.Jedi","key":{},"action":"LOAD","data":{"name":"Rey","identification":3421,"email":"rey@space.com","gender":"female"}}, headers={kafka_offset=31, scst_nativeHeadersPresent=true, kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@517746f5, deliveryAttempt=1, kafka_timestampType=CREATE_TIME, kafka_receivedMessageKey=null, kafka_receivedPartitionId=0, contentType=application/json, kafka_receivedTopic=sgdp-audit, kafka_receivedTimestamp=1576691551798}]

```

**Consulta de dados pessoais**

Para efetuar a consulta dos dados pessoais de um titular deve ser requisitada a url `localhost:9190/sgdp/v1/data?identification=1` informando a identificação do Jedi.

Os dados podem ser verificados no log do serviço.


**Anonimização de dados pessoais**

Para efetuar a anonimização dos dados pessoais de um titular deve ser requisitada a url `localhost:9190/sgdp/v1/mask?identification=1` informando a identificação do Jedi.

Os dados podem ser verificados no log do serviço.


# Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente SGDP e enviar sugestões e melhorias para o projeto **TOTVS Java Framework**.

[tjf-api-jpa-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[h2]: https://www.h2database.com