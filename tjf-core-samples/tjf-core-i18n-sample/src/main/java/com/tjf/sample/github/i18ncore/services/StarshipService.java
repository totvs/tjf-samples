package com.tjf.sample.github.i18ncore.services;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.i18ncore.domain.Starship;

@Component
public class StarshipService {

	@Autowired
	private ObjectMapper objectMapper;

	public Starship getStarshipInfo(String shipInfo) throws IOException, URISyntaxException {
		ClassLoader classLoader = StarshipService.class.getClassLoader();
		URL resource = classLoader.getResource("json/Falcon.json");
		File file = Paths.get(resource.toURI()).toFile();

		String content = new String(Files.readAllBytes(file.toPath()));
		return objectMapper.readValue(content, Starship.class);
	}

}
