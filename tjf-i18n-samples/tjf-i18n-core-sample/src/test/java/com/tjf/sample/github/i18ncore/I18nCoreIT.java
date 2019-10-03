package com.tjf.sample.github.i18ncore;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tjf.sample.github.i18ncore.domain.Starship;
import com.tjf.sample.github.i18ncore.messages.StarshipMessage;
import com.tjf.sample.github.i18ncore.services.StarshipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {I18nCoreApplication.class})
public class I18nCoreIT {

	@Autowired
	StarshipService starshipService;

	@Autowired
	StarshipMessage starshipMessage;

	String name;

	@Before
	public void prepare() throws IOException {
		Starship starship = starshipService.getStarshipInfo("Falcon.json");
		name = starship.getName();
	}

	@Test
	public void i18n_pt_br_test() throws IOException {

		Locale.setDefault(new Locale("pt", "br"));
		assertEquals("Nave Millennium Falcon, pouso autorizado", starshipMessage.starshipConfirmLanding(name));
	}

	@Test
	public void i18n_en_us_test() throws IOException {

		Locale.setDefault(new Locale("en", "us"));
		assertEquals("Starship Millennium Falcon, authorized landing", starshipMessage.starshipConfirmLanding(name));
	}

	@Test
	public void i18n_es_es_test() throws IOException {

		Locale.setDefault(new Locale("es", "es"));
		assertEquals("Nave Millennium Falcon, aterrizaje autorizado", starshipMessage.starshipConfirmLanding(name));
	}
}