package com.tjf.sample.github.apicore.fields.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.apicore.fields.model.Jedi;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v2.response.ApiCollectionResponse;

@RestController
@RequestMapping(path = JediController.PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class JediController {

	public static final String PATH = "api/v1/jedi";

	@GetMapping(path = "/find")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Jedi> nameGet() throws IOException {
		List<Jedi> jedis = List.of(new Jedi("Luke Skywalker", "Masculino", "Humano", 1.72),
				new Jedi("Anakin Skywalker", "Masculino", "Humano", 1.88),
				new Jedi("Obi-Wan Kenobi", "Masculino", "Humano", 1.82),
				new Jedi("Mace Windu", "Masculino", "Humano", 1.92),
				new Jedi("Yoda", "Masculino", "Desconhecida", 0.66));
		return ApiCollectionResponse.from(jedis);
	}

}
