package com.tjf.sample.github.api.r2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tjf.sample.github.api.r2dbc.controller.DataInit;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.tjf.sample.github.api.r2dbc.model.AccountModelRepository;
import com.tjf.sample.github.api.r2dbc.model.EmployeeModelRepository;

//import com.totvs.tjf.api.jpa.repository.impl.ApiJpaRepositoryImpl;

@SpringBootApplication
//@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class ApiJpaFilterApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiJpaFilterApplication.class, args);
	}
}
