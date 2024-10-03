# Exemplo de Utilização do Parking Lot com o TJF Messaging Stream


_Sample_ de utilização de _functions_ com a biblioteca [__Messaging Stream__][tjf-messaging-stream] do [__TOTVS Java Framework__][tjf].

## Contexto

O objetivo deste exemplo é mostrar como configurar o comportamento de parking lot no TJF, que facilita o gerenciamento de mensagens que falharam durante o processamento.

### Alterações no *sample subscriber*

nicialmente, ativamos o roteamento de functions no subscriber através das propriedades spring.cloud.function.routing.enabled=true e spring.cloud.function.routing-expression=headers['type']. Isso permite que o roteamento seja feito com base no cabeçalho type.

```yml
spring:
  cloud:
    function:
      routing:
       	enabled: true
      routing-expression: headers['type']
```

Em seguida, configuramos dois bindings genéricos para o recebimento de mensagens e os bindings de erro conforme o exemplo:


```yml
        functionRouter-in-0:
          binder: rabbit1
          destination: starship-input
          group: request
        functionRouter-in-1:
          destination: starship-input.request
          group: dlq     
    # Filas de Erros
        tjf-messaging-error-in-0:
          destination: bbunit-errors
          group: errors

        tjf-messaging-error-out-0:
          destination: bbunit-errors
          group: errors
          producer:
            required-groups: errors

```
> O nome _functionRouter_ é fixo, conforme documentação do String.

Para que os dois binding genéricos funcionem é necessário ativar a propriedade do TJF: ´tjf.messging.function-router.multiple-inputs=true´

```yaml
tjf:
  messaging:
    function-router:
      multiple-inputs: true 
```

#### Configurando Consumers e DLQ

Agora, configuramos os consumers para garantir que, ao criar uma fila, sua respectiva fila DLQ seja criada automaticamente:

```yml
 bindings:                          
      binders:
        rabbit1:
          type: rabbit

      rabbit:
        bindings:
          functionRouter-in-0:
            consumer:
              exchange-type: headers
              republish-to-dlq: true
              auto-bind-dlq: true
          functionRouter-in-1:
            consumer:
              exchange-type: headers
              republish-to-dlq: false
              auto-bind-dlq: false 
```
#### Criando Consumers para Eventos
      
Para cada type de mensagem recebido, criamos um bean que lança uma exceção para simular falhas no processamento:
      
```java
@Bean
public ConsumerWithTenant<TOTVSMessage<StarShipLeftEvent>> StarShipLeftEvent() {
	return message -> {
		System.out.println("StarShipLeftEvent recebido!");

				throw new NullPointerException("Not OK");
	};
}

@Bean
public ConsumerWithTenant<TOTVSMessage<StarShipArrivedEvent>> StarShipArrivedEvent() {
	return message -> {
		System.out.println("StarShipArrivedEvent recebido!");
			throw new NullPointerException("Not OK");
	};
}	
```

## Testando o Exemplo


No nosso exemplo você vai precisar estar com o `RabbitMQ` já configurado e em execução, por conversão como não especificamos as portas ele irá usar as portas padrões.

Execute os nossos dois projetos.

Agora acesse a URL pelo navegador [http://localhost:8280/starship/arrived?name=millenium%20falcon&tenant=Alderaan](http://localhost:8280/starship/arrived?name=millenium%20falcon&tenant=Alderaan), nossa API rest criada irá publicar para a mensageria uma mensagem de evento e nosso outro projeto deve receber a mensagem e mostrar no log, perceba que o tenant também foi carregado com sucesso:

```
StarShip arrived!

Current tenant: Alderaan
Starship name: millenium falcon
Starship ranking: 1
Counter by tenant: 1
```

Agora vamos enviar uma nave saindo do gate [http://localhost:8280/starship/left?name=at&tenant=Alderaan](http://localhost:8280/starship/left?name=at&tenant=Alderaan):

```
StarShip left!

Current tenant: Alderaan
Starship name: at
Starship ranking: 4
Counter by tenant: 0
```

### Comportamento da Mensagem com Falha

Na aplicação subscriber, a exceção será lançada três vezes (valor padrão de tentativas) antes de a mensagem ser movida para a fila DLQ e, posteriormente, para a fila de erros (parking lot), bbunit-errors.


Acompanhe junto ao rabbitMQ Management o comportamento da mensagem, indo da fila principal para a DLQ repetindo as 3 retentativas e então aprando na fila de erros(parking-lot) que configuramos bbunit-errors.


#### Reprocessando Mensagens da Fila de Erros


Para retirar a mensagem da fila  de erros e reenviá-la à fila DLQ, acesse o endpoint [http://localhost:8380/_/messaging](http://localhost:8380/_/messaging). A fila principal permanecerá livre para novas mensagens.

> Para mais informações sobre a definição dos campos, veja a documentação do padrão de CloudEvents no Modelo base de mensagem para comunicação entre serviços da TOTVS na [RFC000011].

[tjf-messaging-stream]: https://tjf.totvs.com.br/wiki/v3/tjf-messaging-stream
[tjf]: https://tjf.totvs.com.br
[RFC000011]: https://arquitetura.totvs.io/architectural-records/RFCs/Corporativas/RFC000011/
