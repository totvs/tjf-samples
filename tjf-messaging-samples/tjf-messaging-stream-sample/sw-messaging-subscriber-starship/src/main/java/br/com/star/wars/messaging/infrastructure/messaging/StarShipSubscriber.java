package br.com.star.wars.messaging.infrastructure.messaging;

import static br.com.star.wars.messaging.infrastructure.messaging.StarShipExchange.INPUT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.star.wars.messaging.model.StarShip;
import br.com.star.wars.messaging.services.StarShipService;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	public static final transient String NAME = "arrivedStarShip";

	public static final transient String CONDITIONAL_EXPRESSION = "headers['event']=='" + NAME + "'";

	@Value("new br.com.star.wars.messaging.model.StarShip('" + NAME + "').getName()")
	public static String CONDITIONAL_EXPRESSION2;
	
	private StarShipService starShipService;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
	}

	//"headers['type']=='arrivedStarShip'"
	
	//@Teste(target = StarShipExchange.INPUT, name = NAME)
	//headers['startDate'] < new java.util.Date().time
    
    //@StreamListener(target = INPUT, condition = "headers['startDate'] < new java.util.Date().getTime()")
	@StreamListener(target = INPUT, condition = "headers['type'] == new br.com.star.wars.messaging.model.StarShip('" + NAME + "').getName()")
	
	//@Teste(target = INPUT)
	public void subscribe(TOTVSMessage<StarShip> starShip) {
		starShipService.arrived(starShip.getContent());
	}
	
	public static long teste() {
		
		System.out.println("######### TESTE #############");
		
		return new java.util.Date().getTime();
	}
}
