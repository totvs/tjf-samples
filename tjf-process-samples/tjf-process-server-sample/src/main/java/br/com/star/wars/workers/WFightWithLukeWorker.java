package br.com.star.wars.workers;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.listener.PlanItemInstanceLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.process.server.core.ProcessServerMessage;

import br.com.star.wars.commands.FightWithLukeCommand;
import br.com.star.wars.messaging.AnakinExchange;

@Component
public class WFightWithLukeWorker implements PlanItemInstanceLifecycleListener {
	
	static final Logger LOG = LoggerFactory.getLogger(WMarryWithPadmeWorker.class);
	
    @Autowired
    private AnakinExchange exchange;
	
	@Override
	public String getSourceState() {
		return null;
	}

	@Override
	public String getTargetState() {
		return null;
	}

	@Override
	public void stateChanged(DelegatePlanItemInstance planItemInstance, String oldState, String newState) {

		LOG.info("To Anakin: {}", "FightWithLukeCommand\n");

		FightWithLukeCommand command = new FightWithLukeCommand();

		new ProcessServerMessage <FightWithLukeCommand> ("FightWithLukeCommand", planItemInstance)
				.with(command)
				.sendTo(exchange.output());	
		
	}

}