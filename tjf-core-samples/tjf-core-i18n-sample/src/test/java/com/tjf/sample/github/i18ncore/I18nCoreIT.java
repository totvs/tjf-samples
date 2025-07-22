package com.tjf.sample.github.i18ncore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tjf.sample.github.i18ncore.domain.Starship;
import com.tjf.sample.github.i18ncore.messages.StarshipMessage;
import com.tjf.sample.github.i18ncore.services.StarshipService;

@SpringBootTest(classes = { I18nCoreApplication.class })
public class I18nCoreIT {

	@Autowired
	private StarshipService starshipService;

	@Autowired
	private StarshipMessage starshipMessage;

	private String name;

	@BeforeEach
	public void prepare() throws Exception {
		Starship starship = starshipService.getStarshipInfo("Falcon.json");
		name = starship.getName();
	}

	@Test
	public void testPtBR() throws Exception {
		Locale.setDefault(new Locale("pt", "br"));
		assertEquals("Nave Millennium Falcon, pouso autorizado", starshipMessage.starshipConfirmLanding(name));
	}

	@Test
	public void testEnUS() throws Exception {
		Locale.setDefault(new Locale("en", "us"));
		assertEquals("Starship Millennium Falcon, authorized landing", starshipMessage.starshipConfirmLanding(name));
	}

	@Test
	public void testEsES() throws Exception {
		Locale.setDefault(new Locale("es", "es"));
		assertEquals("Nave Millennium Falcon, aterrizaje autorizado", starshipMessage.starshipConfirmLanding(name));
	}

}