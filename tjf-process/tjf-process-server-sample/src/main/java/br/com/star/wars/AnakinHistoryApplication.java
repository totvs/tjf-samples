package br.com.star.wars;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.totvs.tjf.cmmn.model.TCaseModel;
import com.totvs.tjf.dmn.model.TDecision;
import com.totvs.tjf.mock.test.MockAuthenticationInfo;
import com.totvs.tjf.process.model.TModel;
import com.totvs.tjf.process.server.core.ProcessDeployer;

import br.com.star.wars.messaging.AnakinExchange;
import br.com.star.wars.messaging.StarWarsExchange;

@SpringBootApplication()
@ComponentScan("br.com.star.wars")
@EnableAsync
@EnableBinding({ StarWarsExchange.class, AnakinExchange.class })
public class AnakinHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnakinHistoryApplication.class, args);
	}

	@Autowired
	private ProcessDeployer processDeployer;
	
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void doSomethingAfterStartup() {
		
		MockAuthenticationInfo.setAuthenticationInfo("starwars");
		
		show("starwars.txt");
		deployBPMN();
		deployDMN();
		deployCMMN();
		
	}

	private void deployBPMN () {

		TModel model = new TModel("AnakinSkyWalkerProcess");
		model.getProcess()
			.newStartEvent("startEvent", "QuiGonFoundAnakinEvent").flowsTo()
			.newTransaction("transaction")
				.newStartEvent("startEventTransaction").flowsTo()
				.newUserTask("marryWithPadme", "${WMarryWithPadmeWorker}").roles("ANAKIN").flowsTo()
				.newDecisionTask("decideSide", "ForceSideDecision").flowsTo()
				.newExclusiveGateway("whichSide")
					.when("${side == 'light'}", "wrongEnd")
					.when("${side == 'dark'}", "becomeDarthVader")
					.flowsToCondition()
				.newReceiveTask("becomeDarthVader", "${WBecomeDarthVaderWorker}").flowsTo()
				.newCaseTask("oldTrilogy", "OldTrilogyCase").flowsTo()
				.newEndEvent("endEvent", "${WEndWorker}").noFlow()
				.newCancelEvent("wrongEnd", null);
		
		processDeployer.deploy(model.getElement());
		System.out.println("\n\t\t- ANAKIN SKYWALKER PROCESS -\n");
		System.out.println(model.toXML());
		
	}
	
	private void deployDMN () {
		
		TDecision decision = new TDecision("ForceSideDecision");
		decision.addInput("killSandPeople", "boolean")
				.addInput("killMaceWindu", "boolean")
				.addOutput("side", "string")
				.addRule()
					.addInput("false")
					.addInput("false")
					.addOutput("'light'")
				.addRule()
					.addInput("true")
					.addInput("false")
					.addOutput("'dark'")
				.addRule()
					.addInput("true")
					.addInput("true")
					.addOutput("'dark'");

		processDeployer.deploy(decision.getDefinition());
		System.out.println("\n\t\t- ANAKIN SIDE FORCE DECISION -\n");
		System.out.println(decision.toXML());
				
	}
	
	private void deployCMMN () {
		
		TCaseModel model = new TCaseModel("OldTrilogyCase");
		model.addStage("container")
			.addTask("visitDeathStar", "VisitDeathStar", "${WVisitDeathStarWorker}")
			.and()
			.addTask("revealToLuke", "RevealToLuke", "${WRevealToLukeWorker}")
			.and()
			.addTask("fightWithLuke", "FightWithLuke", "${WFightWithLukeWorker}");
		
		processDeployer.deploy(model.getElement());
		System.out.println("\n\t\t- OLD TRILOGY SEQUENCE CASE -\n");
		System.out.println(model.toXML());
		
	}

	public static void show (String filename) {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			AnakinHistoryApplication.class.getClassLoader().getResourceAsStream(filename).transferTo(baos);
			System.out.println(baos.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String toJSONString (Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
}