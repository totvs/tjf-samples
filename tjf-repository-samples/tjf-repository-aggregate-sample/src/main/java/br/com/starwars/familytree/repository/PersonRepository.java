package br.com.starwars.familytree.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

import br.com.starwars.familytree.model.Person;

@Repository
public class PersonRepository extends CrudAggregateRepository<Person, String> {

	public PersonRepository(EntityManager em, ObjectMapper mapper) {
		super(em, mapper);
	}

}
