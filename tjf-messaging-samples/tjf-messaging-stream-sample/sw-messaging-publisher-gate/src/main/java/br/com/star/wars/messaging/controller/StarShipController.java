package br.com.star.wars.messaging.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.star.wars.messaging.infrastructure.messaging.StarShipPublisher;
import br.com.star.wars.messaging.interfaces.StarShip;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {
	
	private StarShipPublisher samplePublisher;
	
	public StarShipController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}
	
	@GetMapping
    String getIdByValue(@RequestParam("name") String name) {
        
		System.out.println("Starship name is " + name);
        
        StarShip starShip = new StarShip(name);        
        
        samplePublisher.publish(starShip);
        
        return "The identification of the starship " + name + " was sent!";
    }
}