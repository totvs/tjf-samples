package com.tjf.sample.github.i18ncore.application;

import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

@Component
public class MainGate implements CommandLineRunner {

	@Autowired
	private AuthorizedGate authorizedGate;

	@Override
	public void run(String... args) throws Exception {
		enableI18nDebugLog();

		Locale.setDefault(new Locale("pt", "br"));
		System.out.println(authorizedGate.authorizedShipLanding("Falcon.json"));
		Locale.setDefault(new Locale("en", "us"));
		System.out.println(authorizedGate.authorizedShipLanding("Xwing.json"));
		Locale.setDefault(new Locale("es", "es"));
		System.out.println(authorizedGate.authorizedShipLanding("Ywing.json"));
	}

	public void enableI18nDebugLog() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = loggerContext.getLogger("com.totvs.tjf.i18n");
		rootLogger.setLevel(Level.DEBUG);
	}

}
