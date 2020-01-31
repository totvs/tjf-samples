package br.com.star.wars.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.star.wars.AnakinApplication;
import br.com.star.wars.commands.BecomeDarthVaderCommand;
import br.com.star.wars.commands.FightWithLukeCommand;
import br.com.star.wars.commands.RevealToLukeCommand;
import br.com.star.wars.commands.MarryWithPadmeCommand;
import br.com.star.wars.commands.VisitDeathStarCommand;
import br.com.star.wars.events.BecomeDarthVaderEvent;
import br.com.star.wars.events.FightWithLukeEvent;
import br.com.star.wars.events.RevealToLukeEvent;
import br.com.star.wars.events.MarryWithPadmeEvent;
import br.com.star.wars.events.VisitDeathStarEvent;
import br.com.star.wars.messaging.StarWarsPublisher;

@Service
public class AnakinSkywalkerService {

	@Autowired
    private StarWarsPublisher publisher;
	
	public void handle (MarryWithPadmeCommand command) {
		
		AnakinApplication.show("marriage.txt");
		
		// The marriage location is just set by the tjf-process-server-sample
		// where using the tfj-process-server is it possible to set it in the
		// MarryWithPadmeCommand.
		// To have this information using the tjf-process-service-sample it
		// is necessary to send it with a subsequent event, in our case the
		// QuiGonFoundAnakinEvent
		System.out.println("At: " + command.getPlace() + "\n\n");
		
		AnakinApplication.wait(2000);
		
		MarryWithPadmeEvent event = MarryWithPadmeEvent.from(command);
		event.setKillSandPeople(true);
		event.setKillMaceWindu(true);
		publisher.dispatch(event);
		
	}

	public void handle (BecomeDarthVaderCommand command) {
		
		AnakinApplication.show("darthvader.txt");
		AnakinApplication.wait(2000);
		publisher.dispatch(new BecomeDarthVaderEvent());
		
	}

	public void handle (VisitDeathStarCommand command) {
		
		AnakinApplication.show("deathstar.txt");
		AnakinApplication.wait(2000);
		publisher.dispatch(new VisitDeathStarEvent());
		
	}

	public void handle (RevealToLukeCommand command) {
		
		AnakinApplication.show("iamyourfather.txt");
		AnakinApplication.wait(2000);
		publisher.dispatch(new RevealToLukeEvent());
		
	}

	public void handle (FightWithLukeCommand command) {
		
		AnakinApplication.show("fightwithluke.txt");
		AnakinApplication.wait(2000);
		publisher.dispatch(new FightWithLukeEvent());
		
	}
	
}