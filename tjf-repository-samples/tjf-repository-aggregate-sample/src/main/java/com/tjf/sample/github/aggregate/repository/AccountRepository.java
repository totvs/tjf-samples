package com.tjf.sample.github.aggregate.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.aggregate.model.AccountModel;
import com.totvs.tjf.repository.aggregate.CrudAggregateRepository;

@Repository
public class AccountRepository extends CrudAggregateRepository<AccountModel, String> {

    public AccountRepository(EntityManager em, ObjectMapper mapper) {
        super(em, mapper);
    }
    
    @Override
    protected String getTableName() {
        return "sample_account";
    }
    
}
