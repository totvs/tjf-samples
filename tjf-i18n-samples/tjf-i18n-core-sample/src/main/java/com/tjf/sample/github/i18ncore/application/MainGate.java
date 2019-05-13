package com.tjf.sample.github.i18ncore.application;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MainGate implements CommandLineRunner {

	@Autowired
	AuthorizedGate authorizedGate;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(authorizedGate.authorizedShipLanding("Falcon.json"));
		Locale.setDefault(new Locale("en", "us"));
		System.out.println(authorizedGate.authorizedShipLanding("Xwing.json"));
		Locale.setDefault(new Locale("es", "es"));
		System.out.println(authorizedGate.authorizedShipLanding("Ywing.json"));
	}

}
