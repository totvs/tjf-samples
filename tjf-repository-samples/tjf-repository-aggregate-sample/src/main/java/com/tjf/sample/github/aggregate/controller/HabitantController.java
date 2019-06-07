package com.tjf.sample.github.aggregate.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tjf.sample.github.aggregate.infra.Habitant;
import com.tjf.sample.github.aggregate.infra.HabitantRepository;

@RestController
@RequestMapping(path = "/api/v1/habitants", produces = MediaType.APPLICATION_JSON_VALUE)
public class HabitantController {
	
	@Autowired
	private HabitantRepository repository;
				
	@PostMapping
	@Transactional
	public void postAccount(@RequestBody Habitant dtos) {

		repository.store(dtos);
				
	}

}
