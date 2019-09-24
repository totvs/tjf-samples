package br.com.star.wars.workers;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.star.wars.events.EndProcessEvent;
import br.com.star.wars.messaging.AnakinExchange;

@Component
public class WEndWorker implements JavaDelegate {

    @Autowired
    private AnakinExchange exchange;
    
	@Override
	public void execute(DelegateExecution execution) {

		new TOTVSMessage <EndProcessEvent> ("EndProcessEvent", new EndProcessEvent())
			.sendTo(exchange.output());	
		
	}

}
