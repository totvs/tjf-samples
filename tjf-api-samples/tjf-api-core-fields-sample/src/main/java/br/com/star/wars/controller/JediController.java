package br.com.star.wars.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.context.v1.response.ApiCollectionResponse;

import br.com.star.wars.model.Jedis;

@RestController
@RequestMapping(path = JediController.PATH, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class JediController {

	public static final String PATH = "api/v1/jedi";

	@GetMapping(path = "/find")
	@ResponseStatus(code = HttpStatus.OK)
	public ApiCollectionResponse<Jedis> nameGet() throws IOException {

		return ApiCollectionResponse.of(List.of(new Jedis("Luke Skywalker", "Masculino", "Humano", 1.72),
				new Jedis("Anakin Skywalker", "Masculino", "Humano", 1.88),
				new Jedis("Obi-Wan Kenobi", "Masculino", "Humano", 1.82),
				new Jedis("Mace Windu", "Masculino", "Humano", 1.92),
				new Jedis("Yoda", "Masculino", "Desconhecida", 0.66)));
	}

}