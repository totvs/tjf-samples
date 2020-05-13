package com.tjf.sample.github.repositoryaggregate.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.repositoryaggregate.model.Person;
import com.tjf.sample.github.repositoryaggregate.repository.PersonRepository;

@RestController
@RequestMapping(path = "api/v1/person", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

	@Autowired
	private PersonRepository repository;

	@PostMapping
	public void insert(@RequestBody Person person) {
		repository.insert(person);
	}

}
