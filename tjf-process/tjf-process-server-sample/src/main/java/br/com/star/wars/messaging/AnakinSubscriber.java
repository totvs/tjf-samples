package br.com.star.wars.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.process.core.ProcessMessage;
import com.totvs.tjf.process.server.core.ProcessManager;
import com.totvs.tjf.process.server.core.ProcessState;

import br.com.star.wars.AnakinHistoryApplication;
import br.com.star.wars.events.BecomeDarthVaderEvent;
import br.com.star.wars.events.FightWithLukeEvent;
import br.com.star.wars.events.RevealToLukeEvent;
import br.com.star.wars.events.MarryWithPadmeEvent;
import br.com.star.wars.events.QuiGonFoundAnakinEvent;
import br.com.star.wars.events.VisitDeathStarEvent;

@EnableBinding(StarWarsExchange.class)
public class AnakinSubscriber {

	static final Logger LOG = LoggerFactory.getLogger(AnakinSubscriber.class);
	
	@Autowired
	private ProcessManager processManager;
	
	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='QuiGonFoundAnakinEvent'")
    public void onCreated(TOTVSMessage <QuiGonFoundAnakinEvent> message) {
		
		LOG.info("*** Episode I: The Phantom Menace ***");
		LOG.info(AnakinHistoryApplication.toJSONString(message) + "\n");
		processManager.startProcessByMessage("QuiGonFoundAnakinEvent", new ProcessState());
		
    }
	
	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='MarryWithPadmeEvent'")
    public void onMarryEvent(ProcessMessage <MarryWithPadmeEvent> event) {
		
		LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");
		
		// This information below will be used by the Force Side Decision
		ProcessState state = new ProcessState();
		state.put("killSandPeople", event.getContent().isKillSandPeople());
		state.put("killMaceWindu", event.getContent().isKillMaceWindu());
		
		processManager.completeTask(event, state);
		
	}

	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='BecomeDarthVaderEvent'")
    public void onVaderEvent(ProcessMessage <BecomeDarthVaderEvent> event) {

		LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");
		processManager.completeActivity(event);
		
	}

	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='VisitDeathStarEvent'")
    public void onVisitEvent(ProcessMessage <VisitDeathStarEvent> event) {

		LOG.info("*** Episode IV: A New Hope ***");
		LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");
		processManager.completePlanItem(event, null);
		
	}

	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='RevealToLukeEvent'")
    public void onRevealEvent(ProcessMessage <RevealToLukeEvent> event) {
		
		LOG.info("*** Episode V: The Empire Strikes Back ***");
		LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");
		processManager.completePlanItem(event, null);
		
	}
	
	@StreamListener(target = StarWarsExchange.INPUT, condition = "headers['type']=='FightWithLukeEvent'")
    public void onFightEvent(ProcessMessage <FightWithLukeEvent> event) {
		
		LOG.info("*** Episode VI: Return of the Jedi ***");
		LOG.info(AnakinHistoryApplication.toJSONString(event) + "\n");
		processManager.completePlanItem(event, null);
		
	}

}