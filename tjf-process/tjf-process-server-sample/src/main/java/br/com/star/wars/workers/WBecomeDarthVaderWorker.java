package br.com.star.wars.workers;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.process.server.core.ProcessServerMessage;

import br.com.star.wars.commands.BecomeDarthVaderCommand;
import br.com.star.wars.messaging.AnakinExchange;

@Component
public class WBecomeDarthVaderWorker implements JavaDelegate {

	static final Logger LOG = LoggerFactory.getLogger(WBecomeDarthVaderWorker.class);
	
    @Autowired
    private AnakinExchange exchange;
	
	@Override
	public void execute(DelegateExecution delegateExecution) {
		
		LOG.info("*** Episode III: Revenge of the Sith ***");
		LOG.info("To Anakin: {}", "BecomeDarthVaderCommand");

		BecomeDarthVaderCommand command = new BecomeDarthVaderCommand();

		new ProcessServerMessage <BecomeDarthVaderCommand> ("BecomeDarthVaderCommand", delegateExecution)
				.with(command)
				.sendTo(exchange.output());		
	}

}