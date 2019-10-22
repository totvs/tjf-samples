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
import br.com.star.wars.messaging.events.StarShipArrivedWithoutTenantEvent;
import br.com.star.wars.messaging.events.StarShipLeftEvent;
import br.com.star.wars.messaging.infrastructure.messaging.StarShipPublisher;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private StarShipPublisher samplePublisher;

	public StarShipController(StarShipPublisher samplePublisher) {
		this.samplePublisher = samplePublisher;
	}

	@GetMapping("/arrived")
	String starShipArrived(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship arrived name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);
		samplePublisher.publish(starShipEvent, StarShipArrivedEvent.NAME);

		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!";
	}

	@GetMapping("/arrivedWithoutTenant")
	String starShipArrived(@RequestParam("name") String name) {

		this.setTenant(null);

		System.out.println("\nStarship arrived name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		StarShipArrivedWithoutTenantEvent starShipEvent = new StarShipArrivedWithoutTenantEvent(name);
		samplePublisher.publish(starShipEvent, StarShipArrivedWithoutTenantEvent.NAME);

		return "The identification of the arrived starship " + name + " without tenant was sent!";
	}

	@GetMapping("/left")
	String starShipLeft(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship left name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		StarShipLeftEvent starShipEvent = new StarShipLeftEvent(name);
		samplePublisher.publish(starShipEvent, StarShipLeftEvent.NAME);

		return "The identification of the left starship " + name + " of tenant " + tenant + " was sent!";
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A",
				null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}