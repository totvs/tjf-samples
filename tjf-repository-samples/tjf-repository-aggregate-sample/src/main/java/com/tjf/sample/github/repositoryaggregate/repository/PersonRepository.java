package com.tjf.sample.github.repositoryaggregate.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.repositoryaggregate.model.Person;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

@Repository
public class PersonRepository extends CrudAggregateRepository<Person, String> {

	public PersonRepository(EntityManager em, ObjectMapper mapper) {
		super(em, mapper);
	}

}
