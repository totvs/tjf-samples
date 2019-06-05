package com.totvs.tjf.messaging.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.messaging.infrastructure.messaging.StarShipPublisher;
import com.totvs.tjf.messaging.interfaces.StarShip;

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