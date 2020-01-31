package br.com.star.wars.workers;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.process.server.core.ProcessServerMessage;

import br.com.star.wars.commands.MarryWithPadmeCommand;
import br.com.star.wars.messaging.AnakinExchange;

@Component
public class WMarryWithPadmeWorker implements TaskListener {
	
	private static final long serialVersionUID = 7771924945386171838L;

	static final Logger LOG = LoggerFactory.getLogger(WMarryWithPadmeWorker.class);
	
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

}