# Exemplo de uso do componente Messaging Stream (Filas de Erro)

Exemplo de utilização das filas de erro utilizando o modulo **Messaging Stream**.

## Contexto

Para exemplificar a utilização das filas de erro, iremos utilizar uma unidade **BB** que será responsável por garantir a segurança da mensagem contendo a localização do **Luke Skywalker**.

## Começando

Para criação deste exemplo vamos iniciar a explicação a partir de um projeto Spring já criado, caso você não possua um projeto criado basta acessar o [Spring initializr](https://start.spring.io/) e criar o projeto.

### Dependências

Após gerado, precisamos realizar pequenas alterações e inserir algumas dependências no arquivo `pom.xml` do projeto.

Primeiro, altere _parent_ do projeto para utilizar o **Boot Starter do TJF**:

```xml
<parent>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-boot-starter</artifactId>
  <version>2.6.0-RELEASE</version>
  <relativePath />
</parent>
```

E adicione as dependências abaixo:

```xml
<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-api-core</artifactId>
</dependency>

<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-core-validation</artifactId>
</dependency>

<dependency>
  <groupId>com.totvs.tjf</groupId>
  <artifactId>tjf-messaging-stream</artifactId>
</dependency>
```

Adicione também o repositório Maven de _release_ do TJF:

```xml
<repositories>
  <repository>
    <id>central-release</id>
    <name>TOTVS Java Framework: Release</name>
    <url>http://maven.engpro.totvs.com.br/artifactory/libs-release</url>
  </repository>
</repositories>
```

## Codificando

Antes de iniciarmos a codificação, vamos configurar o uso das filas de mensagens, para isso renomeie o arquivo `application.properties` para `application.yml` e insira as configurações conforme abaixo:

```yaml
spring:
  cloud:
    stream:
      defaultBinder: rabbit1

    bindings:
        # Filas de Execução
        bbunit-input:
          destination: bbunit-input
          group: requests
          consumer:
            max-attempts: 1
        bbunit-output:
          destination: bbunit-input

    # Filas de Erros
        tjf-messaging-error-input:
          destination: bbunit-errors
          group: errors
        tjf-messaging-error-output:
          destination: bbunit-errors
          group: errors
          producer:
            requiredGroups: errors

      binders:
        rabbit1:
          type: rabbit
          environment:
            spring:
              rabbit:
                host: localhost

management:
  endpoints:
    web:
      exposure:
        include: info,health,messaging

server:
  port: 8080
```

> Para compreender de forma exata cada parâmetro utilizado, recomendamos a leitura da documentação do módulo [**TJF Messaging Stream**](https://tjf.totvs.com.br/wiki/tjf-messaging-stream).

Agora estamos prontos para começarmos nosso código fonte, vamos iniciar criando a unidade **BB** que será responsável para criação do nosso objeto:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BBUnit {

  @NotBlank
  private String name;

  private String partner;

  @NotBlank
  private String mission;

  @NotBlank
  private String message;

}
```

Após a criação do modelo, criaremos nossa API REST:

```java
@RestController
@RequestMapping(path = BBUnitMissionController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class BBUnitMissionController {

  public static final String PATH = "mission";

  @Autowired
  private BBUnitPublisher bbPublisher;

  @PostMapping(path = "/send")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ApiCollectionResponse<BBUnit> sendMessageInBBUnit(@RequestBody BBUnit bbUnit) {
    BBUnitSendMission missionEvent = new BBUnitSendMission(bbUnit);
    bbPublisher.publish(missionEvent.getBbUnit(), BBUnitSendMission.MISSION);
    return ApiCollectionResponse.of(List.of(bbUnit));
  }

}
```

Feito isso, vamos criar a classe de exceção que usaremos para informar os erros de validação da nossa unidade **BB**:

```java
@ApiError("BBUnitException")
public class BBUnitException extends ConstraintViolationException {

  private static final long serialVersionUID = -3867016605481005147L;

  public BBUnitException(Set<? extends ConstraintViolation<?>> constraintViolations) {
    super(constraintViolations);
  }

}
```

Dessa forma, ao receber uma mensagem com informações inválidas a mesma será enviada para fila de erros, mas para termos um controle das mensagens, temos que criar a classe de eventos. Estas classes são direcionadas para os eventos esperados para a execução de um programa:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BBUnitSendMission {

  public static final String MISSION = "BBUnitSendMission";
  public static final String CONDITIONAL_EXPRESSION = "headers['type']=='" + MISSION + "'";

  private BBUnit bbUnit;

}
```

> Observe que, conforme a documentação do módulo, as constantes serão utilizadas como controladores para o envio e recebimento das mensagens.

Agora podemos criar a infraestrutura de mensagens, nesta criaremos três classes que serão _exchange_, _publisher_ e _listener_. Vamos começar criando nosso _exchange_:

```java
public interface BBUnitExchange {
  String INPUT = "bbunit-input";

  @Input(BBUnitExchange.INPUT)
  SubscribableChannel input();

  String OUTPUT = "bbunit-output";

  @Output(BBUnitExchange.OUTPUT)
  MessageChannel output();
}
```

Agora criaremos nossa classe _publisher_:

```java
@EnableBinding(BBUnitExchange.class)
public class BBUnitPublisher {
  private BBUnitExchange exchange;

  public BBUnitPublisher(BBUnitExchange exchange) {
    this.exchange = exchange;
  }

  public <T> void publish(T event, String eventName) {
    new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
  }
}
```

E por fim criaremos o _listener_ de mensagens:

```java
@EnableBinding(BBUnitExchange.class)
public class BBUnitSubscribe {

  @Autowired
  private ValidatorService validator;

  private static int contError = 0;

  @WithoutTenant
  @StreamListener(target = BBUnitExchange.INPUT, condition = BBUnitSendMission.CONDITIONAL_EXPRESSION)
  public void subscribeMessageMission(TOTVSMessage<BBUnit> message) {
    if (contError == 0) {
      validator.validate(message.getContent()).ifPresent(violations -> {
        contError = 1;
        throw new BBUnitException(violations);
      });
    }
  }

}
```

> Observe que existe uma variável chamada `contError` responsável por forçar que ocorra a exceção do nosso _model_ quando a primeira mensagem for criada (assim a mesma será enviada para fila de erros), após isso as mensagens serão enviadas de forma normal sem ocorrer os erros.

## Vamos testar?

Para testar nossa aplicação precisaremos do seguinte `docker-compose` para a criação do RabbitMq e para podermos visualizar a mensagem de erro:

```yaml
version: '3'

services:
  
  rabbitMq:
    image: rabbitmq:3.7.7-management
    ports:
      - 15672:15672
      - 5672:5672
```

> Para executar o mesmo, no diretório do arquivo execute o comando `docker-compose up -d`.

Agora vamos iniciar nossa aplicação e testá-la. Ao subir a aplicação execute a seguinte requisição:

```http
POST /mission/send HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "name": "BB-8",
  "partner": "Poe Dameron",
  "mission": "Get the location of Luke Skywalker"
}
```

Após executada a requisição, observe que no terminal ocorreu uma exceção na validação do _subscribe_ enviando a mensagem para fila de erros.

Para visualizar esta mensagem acesse o RabbitMq através da url `http://localhost:15672` (usuário e senha de acesso _guest_), vá até a aba **Queues** e será possível ver as filas criadas, observe que a fila **bbunit-errors.errors** possui uma mensagem de erro, a mesma que enviamos em nossa requisição.

Seguindo a documentação do módulo e configuração que fizemos anteriormente, ao executar a requisição a seguir, as mensagens de erro serão reprocessadas, feito isso ao acessar a fila novamente não teremos mais mensagens de erro:

```http
GET /actuator/messaging HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```

E teremos como retorno o número de mensagens reprocessadas:

```json
{
  "messages": 1
}
```

## Que a força esteja com você!

Com isso terminamos nosso exemplo, fique a vontade para incrementar o exemplo utilizando todos os recursos proposto pelo componente **Messaging Stream**, caso necessário utilize nossa [documentação](https://tjf.totvs.com.br/wiki/tjf-messagin-stream) e fique a vontade para mandar sugestões e melhorias para o projeto [TJF](https://tjf.totvs.com.br/).
