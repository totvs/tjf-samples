package com.totvs.tjf.api.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.totvs.tjf.api.jpa.repository.impl.ApiJpaRepositoryImpl;

@SpringBootApplication
@ComponentScan("com.totvs.tjf.api.jpa")
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
public class CashAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashAccountApplication.class, args);
	}

}
