package br.com.star.wars.messaging.services;

import java.util.HashMap;

import com.totvs.tjf.core.common.security.SecurityDetails;

import br.com.star.wars.messaging.model.StarShip;

public class StarShipService {

	private final HashMap<String, Integer> starShips = new HashMap<String, Integer>();
	
	public StarShipService() {
		starShips.put("millenium falcon", 1);
		starShips.put("star destroyer", 2);
		starShips.put("j-type 327", 3);
		starShips.put("at", 4);
		starShips.put("snowspeeder", 5);
		starShips.put("tie figther", 6);
		starShips.put("naboo starfigther", 7);
		starShips.put("b-wing", 8);
		starShips.put("speeder bike", 9);
		starShips.put("x-wing", 10);
	}
	
	public void arrived(StarShip starShip) {
		
		int rank = starShips.getOrDefault(starShip.getName().toLowerCase(), 0);
		
		System.out.println("\nCurrent Tenant: " + SecurityDetails.getTenant());
		System.out.println("Starship name: " + starShip.getName());
		System.out.println("Starship ranking: " + (rank == 0 ? "Unknown" : rank));
	}
}