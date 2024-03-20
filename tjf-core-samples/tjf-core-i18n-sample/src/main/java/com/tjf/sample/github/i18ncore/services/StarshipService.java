package com.tjf.sample.github.i18ncore.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.i18ncore.domain.Starship;

@Component
public class StarshipService {

	@Autowired
	private ObjectMapper objectMapper;

	public Starship getStarshipInfo(String shipInfo) throws IOException {
		ClassLoader classLoader = StarshipService.class.getClassLoader();
		File file = new File(classLoader.getResource("json/" + shipInfo).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		return objectMapper.readValue(content, Starship.class);
	}

}
