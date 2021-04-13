package com.tjf.sample.github.api.r2dbc.model;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//import com.totvs.tjf.api.jpa.repository.ApiJpaRepository;

//@Repository
//@Transactional
public interface EmployeeModelRepository
		extends ReactiveCrudRepository<EmployeeModel, Integer>

//ApiJpaRepository<EmployeeModel> 
{
}
