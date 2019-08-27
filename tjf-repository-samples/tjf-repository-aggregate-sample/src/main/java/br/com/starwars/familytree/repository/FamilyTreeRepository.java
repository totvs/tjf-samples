package br.com.starwars.familytree.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

import br.com.starwars.familytree.model.FamilyTree;

@Repository
public class FamilyTreeRepository extends CrudAggregateRepository<FamilyTree, String> {

	public FamilyTreeRepository(EntityManager em, ObjectMapper mapper) {
		super(em, mapper);
	}

}
