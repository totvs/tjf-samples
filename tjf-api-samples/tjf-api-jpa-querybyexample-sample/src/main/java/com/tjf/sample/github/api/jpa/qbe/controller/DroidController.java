package com.tjf.sample.github.api.jpa.qbe.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.api.jpa.qbe.model.Droid;
import com.tjf.sample.github.api.jpa.qbe.repository.DroidRepository;
import com.totvs.tjf.api.context.stereotype.ApiGuideline;
import com.totvs.tjf.api.context.stereotype.ApiGuideline.ApiGuidelineVersion;

@RestController
@RequestMapping(path = DroidController.PATH, produces = APPLICATION_JSON_VALUE)
@ApiGuideline(ApiGuidelineVersion.V2)
public class DroidController {

	public static final String PATH = "api/v1/droid";

	@Autowired
	private DroidRepository droidRepository;

	@GetMapping(path = "/findAll")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Droid> findAllDroids() {
		return droidRepository.findAll();
	}

	@GetMapping(path = "/findByName/{name}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Droid> findDroidByName(@PathVariable("name") String name) {
		Droid droid = new Droid();
		droid.setName(name);

		Example<Droid> example = Example.of(droid);

		return droidRepository.findAll(example);
	}

	@GetMapping(path = "/findContainingFunction/{function}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Droid> findDroidLikeDescription(@PathVariable("function") String function) {
		Droid droid = new Droid();
		droid.setFunction(function);

		ExampleMatcher matcher = ExampleMatcher.matchingAll()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreCase();
		Example<Droid> example = Example.of(droid, matcher);

		return droidRepository.findAll(example);
	}
}