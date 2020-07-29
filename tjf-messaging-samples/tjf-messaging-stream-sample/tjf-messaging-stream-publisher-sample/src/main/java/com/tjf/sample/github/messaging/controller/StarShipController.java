package com.tjf.sample.github.messaging.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipArrivedWithoutTenantEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.infrastructure.messaging.StarShipPublisher;
import com.totvs.tjf.core.common.security.SecurityDetails;
import com.totvs.tjf.core.common.security.SecurityPrincipal;
import com.totvs.tjf.core.message.TransactionInfo;

@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

	private StarShipPublisher samplePublisher;

	private static Map<String, Status> transactions = new HashMap<String, Status>();

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

	@GetMapping("/arrived/transacted")
	String starShipArrivedTransacted(@RequestParam("name") String name, @RequestParam("tenant") String tenant) {

		this.setTenant(tenant);

		System.out.println("\nStarship arrived name: " + name);
		System.out.println("Current tenant: " + SecurityDetails.getTenant() + "\n");

		StarShipArrivedEvent starShipEvent = new StarShipArrivedEvent(name);

		String id = UUID.randomUUID().toString();
		TransactionInfo transaction = new TransactionInfo(id, starShipEvent.toString());
		transactions.put(id, Status.SENDED);

		samplePublisher.publish(starShipEvent, StarShipArrivedEvent.NAME, transaction);

		return "The identification of the arrived starship " + name + " of tenant " + tenant + " was sent!\n"
				+ "In transaction " + id + ", acess http://localhost:8080/starship/transaction/" + id
				+ " to consult the status.";
	}

	@GetMapping("/transaction/{id}")
	String starShipArrivedTransacted(@PathVariable("id") String id) {

		Status status = transactions.get(id);

		return status != null ? "Status: " + status.toString() : "Transaction " + id + " not found!";
	}

	@PostMapping("/transaction")
	void closeTransaction(@RequestBody TransactionInfo transaction) {
		transactions.replace(transaction.getTransactionId(), Status.CONCLUDED);
	}

	private void setTenant(String tenant) {
		SecurityPrincipal principal = new SecurityPrincipal(null, "", tenant, tenant);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "N/A",
				null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	enum Status {
		SENDED, CONCLUDED;
	}
}