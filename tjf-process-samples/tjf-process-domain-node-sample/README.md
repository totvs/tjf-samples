# TJF Process Core Node Sample

_Sample_ de uma aplicação escrita em JavaScript (NodeJS) interagindo com um orquestrador do [__TOTVS Java Framework__][tjf].

# Códigos da aplicação

Destacamos abaixo alguns pontos principais de nosso exemplo:

## Preparando o Tenant e iniciando a orquestração

1. A aplicação envia um comando para o orquestrador para a criação do referido tenant.
2. Logo após cria o evento QuiGonFoundAnakinEvent e envia o mesmo para o orquestrador.

```javascript
    publish("workflow-exchange", {
      "header": {
        "type": "CreateTenantCommand",
        "tenantId": "starwars"
      },
      "content": {}
    });
    setTimeout(function () {
      publish("workflow-exchange", {
        "header": {
          "type": "QuiGonFoundAnakinEvent",
          "tenantId": "starwars",
        },
        "content": {
          "message": "I wanna be a Jedi",
          // Uncomment the line below when using the tjf-process-service-sample
          // otherwise the marriage place will be not available in the
          // MarryWithPadmeCommand
          // place: "Varykino Lake Retreat"
          }
      });
    }, 4000);
```

## Recebendo um comando do orquestrador e enviando um evento (de resposta)

Aqui a aplicação se subscreve à um comando do orquestrador, reaproveita a mensagem recebida que contém as informações referentes a atividade, e envia um evento em resposta para o orquestrador.

```javascript
subscribers.MarryWithPadmeCommand = function (message) {
  // The marriage location is just set by the tjf-process-server-sample
  // where using the tfj-process-server is it possible to set it in the
  // MarryWithPadmeCommand.
  // To have this information using the tjf-process-service-sample it
  // is necessary to send it with a subsequent event, in our case the
  // QuiGonFoundAnakinEvent
  show("images/marriage.txt", "At: " + message.content.place);
  message.header.type = "MarryWithPadmeEvent";
  message.content.killSandPeople = true;
  message.content.killMaceWindu = true;  
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};
```

# Executando

Certifique-se que um dos exemplos abaixo já esteja sendo executado:

1. [__tjf-process-server-sample__][tjf-process-server-sample]
2. [__tjf-process-service-sample__][tjf-process-service-sample]

Na pasta principal do exemplo execute os seguintes comando:

```cmd
npm install
npm start
```

Se tudo correr bem você verá uma sequencia de mensagens que representam as atividades de nossa orquestração. Verifique também o log de execução do orquestrador.

# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando os conceitos propostos pela biblioteca [__TJF Process Core__][tjf-process-core] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf]: https://tjf.totvs.com.br
[tjf-process-core]: https://tjf.totvs.com.br/wiki/tjf-process-core
[tjf-process-server-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-server-sample
[tjf-process-service-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-service-sample