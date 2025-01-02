package com.tjf.sample.github.apicore.exception.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.apicore.exception.model.Starship;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;
import com.totvs.tjf.api.validation.stereotype.ApiValid;
import com.totvs.tjf.api.validation.stereotype.ApiValidated;

@RestController
@RequestMapping(path = StarshipControllerV2.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
@ApiValidated(status = HttpStatus.BAD_GATEWAY, value = "StarshipCreateConstraintException")
public class StarshipControllerV2 {

	public static final String PATH = "api/v2/starship";

	@PostMapping(path = "/create")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String createStarship2(@RequestBody @ApiValid Starship dto) {
		return "{\"starship\":\"created\"}";
	}

	@PostMapping(path = "/exception")
	public Starship exceptionStarship2(@RequestBody @ApiValid Starship dto) {
		throw new RuntimeException();
	}
	
	

}