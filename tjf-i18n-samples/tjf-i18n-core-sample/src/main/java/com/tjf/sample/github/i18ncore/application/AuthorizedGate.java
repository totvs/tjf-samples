package com.tjf.sample.github.i18ncore.application;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.i18ncore.domain.Starship;
import com.tjf.sample.github.i18ncore.messages.StarshipMessage;
import com.tjf.sample.github.i18ncore.services.StarshipService;

@Component
public class AuthorizedGate {

	@Autowired
	private StarshipService starshipService;

	@Autowired
	private StarshipMessage starshipMessage;

	private Random random = new Random();

	public String authorizedShipLanding(String shipCard) throws IOException {

		Starship starship = starshipService.getStarshipInfo(shipCard);

		if (random.nextBoolean()) {
			return starshipMessage.starshipConfirmLanding(starship.getName());
		} else {
			return starshipMessage.starshipDestroy(starship.getName());
		}

	}

}
