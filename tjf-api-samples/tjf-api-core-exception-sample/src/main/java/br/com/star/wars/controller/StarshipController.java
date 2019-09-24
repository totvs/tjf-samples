package br.com.star.wars.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.core.validation.ValidatorService;

import br.com.star.wars.exception.StarshipCreateConstraintException;
import br.com.star.wars.model.Starship;

@RestController
@RequestMapping(path = StarshipController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.v1)
public class StarshipController {

	public static final String PATH = "api/v1/starship";

	@Autowired
	private ValidatorService validator;

	@PostMapping(path = "/create")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String createStarship(@RequestBody Starship dto) {

		validator.validate(dto).ifPresent(violations -> {
			throw new StarshipCreateConstraintException(violations);
		});

		return "{\n" + "	\"starship\": \"created\"\n" + "}";

	}

}