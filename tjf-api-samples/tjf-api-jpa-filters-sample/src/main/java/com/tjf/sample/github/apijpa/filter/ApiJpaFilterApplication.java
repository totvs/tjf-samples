package com.tjf.sample.github.apijpa.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.totvs.tjf.api.jpa.repository.impl.ApiJpaRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class ApiJpaFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiJpaFilterApplication.class, args);
	}

}
