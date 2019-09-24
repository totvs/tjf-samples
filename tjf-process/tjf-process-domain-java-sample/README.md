# TJF Process Core Java Sample

_Sample_ de utilização do componente [__TJF Process Core__][tjf-process-core] do [__TOTVS Java Framework__][tjf].

# Códigos da aplicação

Destacamos abaixo alguns pontos principais de nosso exemplo:

## Preparando o Tenant e iniciando a orquestração

1. A aplicação inicia setando o tenant ("starwars") no contexto de segurança do TJF.
2. Em seguida ela envia um comando para o orquestrador para a criação do referido tenant.
3. Logo após cria o evento QuiGonFoundAnakinEvent e envia o mesmo para o orquestrador.

AnakinApplication.java
```java
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		
		show("tatooine.txt");
		
		MockAuthenticationInfo.setAuthenticationInfo("starwars");
		publisher.createTenant();
		wait(4000);
		
		QuiGonFoundAnakinEvent event = new QuiGonFoundAnakinEvent();
		event.setMessage("I wanna be a Jedi");

		// Uncomment the line below when using the tjf-process-service-sample
		// otherwise the marriage place will be not available in the
		// MarryWithPadmeCommand
		
		//event.setPlace("Varykino Lake Retreat");
		
		publisher.dispatch(event);
		
	}
```

## Recebendo um comando do orquestrador

Aqui a aplicação se subscreve à um comando do orquestrador e envia o mesmo para a classe de serviço. A classe _ProcessMessage_ é utilizada aqui para que as informações referentes a atividade seja armazenada no contexto de execução _ProcessContext_.

StarWarsSubscriber.java
```java
  @Autowired
  private AnakinSkywalkerService anakinService;

  @StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='MarryWithPadmeCommand'")
    public void onMarryPadme(ProcessMessage <MarryWithPadmeCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}
```

## Enviando um evento (de resposta) ao orquestrador

Aqui a aplicação envia um evento ao orquestador referente a um comando previamente recebido. A classe _ProcessMessage_ recebe, além do nome do evento, uma referência ao _ProcessContext_, que foi previamente atualizado com as informações da atividade quando do recebimento do comando.

StarWarsPublisher.java
```java
  @Autowired
  private ProcessContext context;

  public void dispatch(final MarryWithPadmeEvent event) {

    new ProcessMessage <MarryWithPadmeEvent> ("MarryWithPadmeEvent", context)
      .with(event)
      .sendTo(exchange.output());
    
  }
```

# Executando

Certifique-se que um dos exemplos abaixo já esteja sendo executado:

1. [__tjf-process-server-sample__][tjf-process-server-sample]
2. [__tjf-process-service-sample__][tjf-process-service-sample]

Na pasta principal do exemplo execute os seguintes comando:

```cmd
mvn clean install
mvn spring-boot:run
```

Se tudo correr bem você verá uma sequencia de mensagens que representam as atividades de nossa orquestração. Verifique também o log de execução do orquestrador.

# Que a força esteja com você!

Com isso terminamos nosso _sample_, fique a vontade para enriquecê-lo utilizando outros recursos propostos pela biblioteca [__TJF Process Core__][tjf-process-core] e enviar sugestões e melhorias para o [__TOTVS Java Framework__][tjf].

[tjf]: https://tjf.totvs.com.br
[tjf-process-core]: https://tjf.totvs.com.br/wiki/tjf-process-core
[tjf-process-server-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-server-sample
[tjf-process-service-sample]: https://github.com/totvs/tjf-samples/tree/master/tjf-process/tjf-process-service-sample