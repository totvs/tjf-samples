package com.tjf.sample.github.messaging.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.messaging.model.StarShip;
import com.totvs.tjf.core.common.security.SecurityDetails;
import com.totvs.tjf.core.message.TransactionInfo;

@Component
public class StarShipService {
	
	RestTemplate rest = new RestTemplate();

	private final HashMap<String, String> starShips = new HashMap<>();
	private final HashMap<String, Integer> counter = new HashMap<>();

	public StarShipService() {
		starShips.put("millenium falcon", "1");
		starShips.put("star destroyer", "2");
		starShips.put("j-type 327", "3");
		starShips.put("at", "4");
		starShips.put("snowspeeder", "5");
		starShips.put("tie figther", "6");
		starShips.put("naboo starfigther", "7");
		starShips.put("b-wing", "8");
		starShips.put("speeder bike", "9");
		starShips.put("x-wing", "10");
	}

	public void arrived(StarShip starShip) {

		String rank = starShips.getOrDefault(starShip.getName(), "Unknown starship!");

		System.out.println("\nStarShip arrived!\n");
		System.out.println("Current tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + rank);
		System.out.println("Counter by tenant: " + arrivedCount());
	}

	public void left(StarShip starShip) {

		String rank = starShips.getOrDefault(starShip.getName(), "Unknown starship!");

		System.out.println("\nStarShip left!\n");
		System.out.println("Current tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + rank);
		System.out.println("Counter by tenant: " + leftCount());
	}
	
	public void transactionClose(TransactionInfo transaction) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map map = new ObjectMapper().convertValue(transaction, Map.class);

		HttpEntity<Map> request = new HttpEntity<Map>(map, headers);

		rest.postForEntity("http://localhost:8080/starship/transaction", request, String.class);
	}

	private int arrivedCount() {

		String tenant = SecurityDetails.getTenant();
		counter.put(tenant, counter.getOrDefault(tenant, 0) + 1);

		return counter.get(tenant);
	}

	private int leftCount() {

		String tenant = SecurityDetails.getTenant();
		counter.put(tenant, counter.getOrDefault(tenant, 0) - 1);

		return counter.get(tenant);
	}
}
