package br.com.star.wars.messaging.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.core.common.security.SecurityDetails;
import com.totvs.tjf.core.common.security.SecurityPrincipal;
import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.star.wars.messaging.infrastructure.messaging.StarShipPublisher;
import br.com.star.wars.messaging.model.StarShip;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {
	
	private StarShipPublisher samplePublisher;
	
	public StarShipController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}
	
	@GetMapping
    String starShip(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {
        
		System.out.println("\nStarship name: " + name);
        
		this.setTenant(tenant);
        System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

        StarShip starShip = new StarShip(name);        
        samplePublisher.publish(starShip);
        
        return "The identification of the starship " + name + " of tenant " + tenant + " was sent!";
    }
	
	private void setTenant(String tenant) {
		
		SecurityPrincipal principal = new SecurityPrincipal("", tenant, tenant.split("-")[0]);
	    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A", null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}