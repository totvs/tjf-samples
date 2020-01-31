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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.totvs.tjf.mock.test.MockAuthenticationInfo;

import br.com.star.wars.events.QuiGonFoundAnakinEvent;
import br.com.star.wars.messaging.AnakinExchange;
import br.com.star.wars.messaging.StarWarsExchange;
import br.com.star.wars.messaging.StarWarsPublisher;

@SpringBootApplication()
@ComponentScan("br.com.star.wars")
@EnableAsync
@EnableBinding({ StarWarsExchange.class, AnakinExchange.class })
public class AnakinApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(AnakinApplication.class, args);
		
	}
	
	@Autowired
	private StarWarsPublisher publisher;
	
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
	
	public static void wait (int miliseconds) {

		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void show (String filename) {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			AnakinApplication.class.getClassLoader().getResourceAsStream(filename).transferTo(baos);
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