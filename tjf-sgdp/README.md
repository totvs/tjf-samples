# SGDP - Sistema de Gestão de Dados Pessoais

# Contexto

Para exemplificar a biblioteca __SGDP__ vamos utilizar o mesmo projeto criado para o _sample_ da biblioteca __API JPA__ disponível em nosso [GitHub][tjf-api-jpa-sample].

# Começando com SGDP

Com o projeto _sample_ __API JPA__ em mãos, vamos alterar alguns campos no banco de dados e incluir as anotações disponibilizadas pelo SGDP na classe de entidade.

> Como _engine_ de banco de dados utilizaremos o [H2][h2].

## Dependências

Incluir a biblioteca como dependência no `pom.xml` da aplicação:

```xml
<dependency>
    <groupId>com.totvs.tjf</groupId>
    <artifactId>tjf-sgdp-core</artifactId>
</dependency>
```

Além das propriedades existentes no `application.yml` da aplicação, será necessário incluir novas propriedades. Conforme o exemplo:

```yml
  cloud:
    stream:
      kafka:
        binder:
          brokers: localhost:9092
      bindings:
        sgdp-kafka-reader:
          destination: sgdp-audit
          binder: kafka
        sgdp-audit:
          destination: sgdp-audit
          contentType: application/json
          binder: kafka
          
      default-binder: kafka
```


## Criando as tabelas

Para que seja possível demonstrar de forma mais efetiva algumas das anotações disponibilizadas no SGDP, precisaremos alterar nossa classe `Jedi.java` e adicionar os campos `identification` (algo como o CPF dos Jedis :D) e `Email`.

```Java
@Entity
public class Jedi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private String name;
	
	@NotNull
	private int identification;
	
	@NotNull
	private String email;

	@NotNull
	private String gender;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIdentification() {
		return globalIdentification;
	}

	public void setIdentification(int identification) {
		this.identification = identification;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
```
Para melhor entendimento, o tutorial será dividido em tópicos, são eles: 

* __Identificação__ - Uso de anotações para identificar dados pessoais;

* __Auditoria__ - Auditoria dos tratamentos realizados em dados pessoais; e 

* __Metadado__ - Extração de relatório do metadado da aplicação.


## Identificação

Agora vamos a inclusão das anotações SGDP, vamos começar com a anotação `@SGDPData` que incluiremos nos campos com dados pessoais. 

Além disso, vamos incluir a `@SGDPPurpose` que define os propósitos do tratamento de um determinado dado pessoal e o `@SGDPDescription` que torna mais claro a descrição da entidade ou atributo durante a geração do metadado.

```java
	@SGDPData(allowsAnonymization = true, isSensitive = true, type = SGDPType.CPF)
	@SGDPPurpose(classification = SGDPClassification.REGULAR_EXERCISE_OF_LAW, justification = "Numero de identificação do Jedi")
	@SGDPDescription("Identification")
	private int identification;
	
	@NotNull
	@SGDPData(allowsAnonymization = true, type = SGDPType.EMAIL)
	@SGDPPurpose(classification = SGDPClassification.CONSENTMENT, justification = "Email para contato.")
	@SGDPPurpose(classification = SGDPClassification.CONTRACT_EXECUTION, justification = "Necessário para contato.")
	@SGDPDescription("Email")
	private String email;
```

> A anotação `@SGDPPurpose` pode ser incluída mais de uma vez por atributo.

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

Será necessário incluir uma nova dependência e uma nova anotação na nossa entidade, conforme exemplos abaixo:

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

Inclusão da anotação `@EntityListeners` na classe `Jedi.java`:

```java
@Entity
@EntityListeners (SGDPSupport.class)
@SGDPDescription("Jedi")
@SGDPCode("Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi")
public class Jedi {
...
}
```

Agora, somente para validação, implementaremos duas classes para verificar o envio das mensagens para o Kafka.

**SGDPKafkaReade.java** - Utilizada como exchange para definir a fila de leitura.

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

**Subscriber.java** - Servirá como listener para interceptarmos as mensagens que são enviadas para o Kafka.

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

Como resultado, ao efetuar um GET no endpoint `localhost:8180/sgpd/v1/jedis`, teremos as seguintes mensagens no _log_.

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


## Metadado

O SGDP conta com um serviço para extração do metadado do banco de dados da aplicação e as identificações feitas com o as anotações SGDP.

Para extrair o metadado conforme descrito na documentação do SGDP, basta realizar um GET no endpoint `/sgdp/metadata`, tendo como resultado o Json abaixo:

```json
{
    "tables": {
        "br.com.star.wars.model.Jedi": {
            "sgdpSupport": true,
            "description": "Jedi",
            "fields": {
                "identification": {
                    "type": "int",
                    "description": "Identification",
                    "sgdpData": {
                        "sensitive": true,
                        "type": "CPF",
                        "allowsAnonymization": true
                    },
                    "sgdpPurposes": [
                        {
                            "classification": "REGULAR_EXERCISE_OF_LAW",
                            "justification": "Numero de identificação do Jedi"
                        }
                    ]
                },
                "gender": {
                    "type": "String",
                    "description": "Gender",
                    "sgdpData": {
                        "sensitive": true,
                        "type": "EMPTY",
                        "allowsAnonymization": false
                    },
                    "sgdpPurposes": []
                },
                "name": {
                    "type": "String",
                    "description": "Nome do Jedi",
                    "sgdpData": null,
                    "sgdpPurposes": []
                },
                "id": {
                    "type": "int",
                    "description": null,
                    "sgdpData": null,
                    "sgdpPurposes": []
                },
                "email": {
                    "type": "String",
                    "description": "Email",
                    "sgdpData": {
                        "sensitive": false,
                        "type": "EMAIL",
                        "allowsAnonymization": true
                    },
                    "sgdpPurposes": [
                        {
                            "classification": "CONSENTMENT",
                            "justification": "Email para contato."
                        },
                        {
                            "classification": "CONTRACT_EXECUTION",
                            "justification": "Necessário para contato."
                        }
                    ]
                }
            },
            "usedAt": []
        }
    },
    "codes": {
        "br.com.star.wars.model.Jedi": {
            "description": "Validar o LGPD do TJF, sobre a identificação, auditoria e anonimização de dados pessoais dos Jedi"
        }
    },
    "package": "br.com.star.wars"
}
```

# Isso é tudo pessoal!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pelo componente [SGDP][tjf-sgdp] e enviar sugestões e melhorias para o projeto __TOTVS Java Framework__.

[tjf-api-jpa-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-api-samples/tjf-api-jpa-sample
[h2]: https://www.h2database.com
[tjf-sgdp]: https://tjf.totvs.com.br/wiki/tjf-sgdp