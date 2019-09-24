# TJF Process Server Sample

_Sample_ de utilização do componente [__TJF Process Server__][tjf-process-server-core] do [__TOTVS Java Framework__][tjf].

# Códigos da aplicação

Destacamos abaixo alguns pontos principais de nosso exemplo:

## Recebendo um evento que inicia uma orquestração

O orquestrador se subscreve à um evento da aplicação e inicia o processo bpmn que tem define como evento de inicio a mensagem "QuiGonFoundAnakinEvent". Poderiamos ainda configurar propriedades do contexto de execução do processo por meio da classe _ProcessState_.

AnakinSubscriber.java
```java
  @Autowired
  private ProcessManager processManager;

  @StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='QuiGonFoundAnakinEvent'")
  public void onCreated(TOTVSMessage <QuiGonFoundAnakinEvent> message) {

    LOG.info("*** Episode I: The Phantom Menace ***");
    LOG.info(AnakinHistoryApplication.toJSONString(message) + "\n");
    processManager.startProcessByMessage("QuiGonFoundAnakinEvent", new ProcessState());

  }
```

## Enviando um comando referente a uma atividade

O engine do orquestrador executada o _Worker_ definido como _Listener_ da atividade corrente, e este por sua vez envia uma _ProcessMessage_ para a aplicação com o comando correspondente.

WMarryWithPadmeWorker.java
```java
  @Autowired
  private AnakinExchange exchange;
	
  @Override
  public void notify(DelegateTask delegateTask) {

    LOG.info("*** Episode II: Attack of the Clones ***");
    LOG.info("To Anakin: {}", "MarryWithPadmeCommand");

    MarryWithPadmeCommand command = new MarryWithPadmeCommand();
    command.setPlace("Varykino Lake Retreat");

    new ProcessServerMessage <MarryWithPadmeCommand> ("MarryWithPadmeCommand", delegateTask)
      .with(command)
      .sendTo(exchange.output());

  }
```

## Recebendo um evento que completando uma atividade

O orquestrador se subscreve à um evento da aplicação que finaliza a atividade correspondente ao comando previamente recebindo, configurando propriedades recebidas no contexto de execução do processo por meio da classe _ProcessState_, que serão utilizadas no modelo de decisão "Force Side Decision".

AnakinSubscriber.java
```java
  @Autowired
  private ProcessManager processManager;

  @StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='MarryWithPadmeEvent'")
  public void onMarryEvent(ProcessMessage <MarryWithPadmeEvent> event) {

    LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");

    // This information below will be used by the Force Side Decision
    ProcessState state = new ProcessState();
    state.put("killSandPeople", event.getContent().isKillSandPeople());
    state.put("killMaceWindu", event.getContent().isKillMaceWindu());

    processManager.completeTask(event, state);

  }
```

# Executando

Na pasta principal do exemplo execute os seguintes comando:

```cmd
sudo docker-compose up -d
mvn clean install
mvn spring-boot:run
```

Execute agora um dos exemplos abaixo:

1. [__tjf-process-domain-java-sample__][tjf-process-domain-java-sample]
2. [__tjf-process-domain-node-sample__][tjf-process-domain-node-sample]

Se tudo correr bem você verá uma sequencia de mensagens que representam as atividades de nossa orquestração. Verifique também o log de execução da aplicação.

# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__TJF Process Server__][tjf-process-server-core] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf]: https://tjf.totvs.com.br
[tjf-process-server-core]: https://tjf.totvs.com.br/wiki/tjf-process-server
[tjf-process-domain-java-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-domain-java-sample
[tjf-process-domain-node-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-domain-node-sample
