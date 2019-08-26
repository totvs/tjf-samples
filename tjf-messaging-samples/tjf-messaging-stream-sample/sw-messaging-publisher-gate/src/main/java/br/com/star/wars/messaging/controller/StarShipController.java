package br.com.star.wars.messaging.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.core.common.security.SecurityDetails;
import com.totvs.tjf.core.common.security.SecurityPrincipal;

import br.com.star.wars.messaging.events.StarShipArrivedEvent;
import br.com.star.wars.messaging.infrastructure.messaging.StarShipPublisher;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {
	
	private StarShipPublisher samplePublisher;
	
	@GetMapping("/Arrived")
    String starShipArrived(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {
        
		this.setTenant(tenant);
        
		System.out.println("\nStarship name: " + name);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShipArrivedEvent starShip = new StarShipArrivedEvent(name);        
        samplePublisher.publish(starShip, StarShipArrivedEvent.NAME);
        
        return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!";
    }
/*
	GenericMessage<T>
	
	@GetMapping("/Left")
    String starShipLeft(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {
        
		this.setTenant(tenant);
        
		System.out.println("\nStarship name: " + name);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShip starShip = new StarShipArrivedEvent(name);        
        samplePublisher.publish(starShip, StarShipArrivedEvent.NAME);
        
        return "The identification of the left starship " + name + " of tenant " + tenant + " was sent!";
    }
*/	
	private void setTenant(String tenant) {
		
		SecurityPrincipal principal = new SecurityPrincipal("", tenant, tenant.split("-")[0]);
	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}