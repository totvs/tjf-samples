package com.tjf.sample.github.repositoryaggregate.api;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.repositoryaggregate.model.Person;
import com.tjf.sample.github.repositoryaggregate.repository.PersonRepository;
import com.totvs.tjf.repository.context.aggregate.AggregateCollectionResult;

@RestController
@RequestMapping(path = "api/v1/person", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {

	@Autowired
	private PersonRepository repository;

	@PostMapping
	public void insert(@RequestBody Person person) {
		repository.insert(person);
	}

	@GetMapping
	public AggregateCollectionResult<Person> readPageable(@NotNull final Pageable pageable) {
		return repository.findAll(pageable, null, null);
	}
}
