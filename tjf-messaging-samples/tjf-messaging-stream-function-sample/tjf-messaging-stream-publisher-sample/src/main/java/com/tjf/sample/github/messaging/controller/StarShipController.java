package com.tjf.sample.github.messaging.controller;

import org.eclipse.sisu.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipArrivedEventWT;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEventWT;
import com.tjf.sample.github.messaging.infrastructure.messaging.StarShipPublisher;
import com.totvs.tjf.core.security.context.SecurityDetails;
import com.totvs.tjf.core.security.context.SecurityPrincipal;
import com.totvs.tjf.messaging.WithoutTenant;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private final StarShipPublisher publisher;

	public StarShipController(StarShipPublisher publisher) {
		this.publisher = publisher;
	}

	@GetMapping("/arrived")
	String starShipArrived(@RequestParam("name") String name, @Nullable @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship arrived name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		if (SecurityDetails.getTenant() == null) {
			StarShipArrivedEventWT starShipEvent = new StarShipArrivedEventWT(name);
			publisher.publishArrivedEvent(starShipEvent);
		} else {
			StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);
			publisher.publishArrivedEvent(starShipEvent);
		}


		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!";
	}

	@GetMapping("/left")
	String starShipLeft(@RequestParam("name") String name, @Nullable @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship left name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");
		if (SecurityDetails.getTenant() == null) {
			StarShipLeftEventWT starShipEvent = new StarShipLeftEventWT(name);
			publisher.publishLeftEvent(starShipEvent);
		} else {
			StarShipLeftEvent starShipEvent = new StarShipLeftEvent(name);
			publisher.publishLeftEvent(starShipEvent);
		}

		return "The identification of the left starship " + name + " of tenant " + tenant + " was sent!";
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A",
				null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}