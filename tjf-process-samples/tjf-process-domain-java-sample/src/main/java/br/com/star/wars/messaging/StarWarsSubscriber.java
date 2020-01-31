package br.com.star.wars.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.process.core.ProcessMessage;

import br.com.star.wars.AnakinApplication;
import br.com.star.wars.app.AnakinSkywalkerService;
import br.com.star.wars.commands.BecomeDarthVaderCommand;
import br.com.star.wars.commands.FightWithLukeCommand;
import br.com.star.wars.commands.MarryWithPadmeCommand;
import br.com.star.wars.commands.RevealToLukeCommand;
import br.com.star.wars.commands.VisitDeathStarCommand;
import br.com.star.wars.events.EndProcessEvent;

@EnableBinding(AnakinExchange.class)
public class StarWarsSubscriber {
	
	static final Logger LOG = LoggerFactory.getLogger(StarWarsSubscriber.class);
	
	@Autowired
	private AnakinSkywalkerService anakinService;

	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='MarryWithPadmeCommand'")
    public void onMarryPadme(ProcessMessage <MarryWithPadmeCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}
	
	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='BecomeDarthVaderCommand'")
    public void onBecomeVader(ProcessMessage <BecomeDarthVaderCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}

	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='VisitDeathStarCommand'")
    public void onVisitStar(ProcessMessage <VisitDeathStarCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}
	
	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='RevealToLukeCommand'")
    public void onReveal(ProcessMessage <RevealToLukeCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}

	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='FightWithLukeCommand'")
    public void onFight(ProcessMessage <FightWithLukeCommand> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		anakinService.handle(command.getContent());
		
	}

	@StreamListener(target = AnakinExchange.INPUT, condition = "headers['type']=='EndProcessEvent'")
    public void onEndProcess(ProcessMessage <EndProcessEvent> command) {

		LOG.info(AnakinApplication.toJSONString(command));
		AnakinApplication.show("theend.txt");
		System.out.println("Press CTRL+C to finish !");
		
	}
	
}