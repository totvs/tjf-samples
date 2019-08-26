package com.tjf.sample.github.aggregate.infra;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

@Repository
public class HabitantModelRepository extends CrudAggregateRepository<HabitantModel, String> {
	
    public HabitantModelRepository(EntityManager em, ObjectMapper mapper) {
        super(em, mapper);
    }
    
    @Override
    protected String getTableName() {
        return "sample_habitant";
    }
    
}
	
