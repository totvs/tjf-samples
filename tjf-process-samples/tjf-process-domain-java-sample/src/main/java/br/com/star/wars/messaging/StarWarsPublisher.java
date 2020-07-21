package br.com.star.wars.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.messaging.TransactionContext;

import com.totvs.tjf.process.core.ProcessMessage;
import com.totvs.tjf.process.service.core.CreateTenantCommand;

import br.com.star.wars.events.BecomeDarthVaderEvent;
import br.com.star.wars.events.FightWithLukeEvent;
import br.com.star.wars.events.MarryWithPadmeEvent;
import br.com.star.wars.events.QuiGonFoundAnakinEvent;
import br.com.star.wars.events.RevealToLukeEvent;
import br.com.star.wars.events.VisitDeathStarEvent;

@EnableBinding(StarWarsExchange.class)
public class StarWarsPublisher {

	@Autowired
	private TransactionContext context;

	@Autowired
	private StarWarsExchange exchange;

	public void createTenant() {
		TOTVSMessage<CreateTenantCommand> message = new TOTVSMessage<CreateTenantCommand>("CreateTenantCommand",
				new CreateTenantCommand() {
				});
		message.sendTo(exchange.output());
	}

	public void dispatch(final QuiGonFoundAnakinEvent event) {
		TOTVSMessage<QuiGonFoundAnakinEvent> message = new TOTVSMessage<QuiGonFoundAnakinEvent>(
				"QuiGonFoundAnakinEvent", event);
		message.sendTo(exchange.output());
	}

	public void dispatch(final MarryWithPadmeEvent event) {
		new ProcessMessage<MarryWithPadmeEvent>("MarryWithPadmeEvent", context).with(event).sendTo(exchange.output());
	}

	public void dispatch(final BecomeDarthVaderEvent event) {
		new ProcessMessage<BecomeDarthVaderEvent>("BecomeDarthVaderEvent", context).with(event)
				.sendTo(exchange.output());
	}

	public void dispatch(final VisitDeathStarEvent event) {
		new ProcessMessage<VisitDeathStarEvent>("VisitDeathStarEvent", context).with(event).sendTo(exchange.output());

	}

	public void dispatch(final RevealToLukeEvent event) {
		new ProcessMessage<RevealToLukeEvent>("RevealToLukeEvent", context).with(event).sendTo(exchange.output());
	}

	public void dispatch(final FightWithLukeEvent event) {
		new ProcessMessage<FightWithLukeEvent>("FightWithLukeEvent", context).with(event).sendTo(exchange.output());
	}

}