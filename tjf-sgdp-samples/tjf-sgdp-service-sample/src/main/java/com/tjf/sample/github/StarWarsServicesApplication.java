package com.tjf.sample.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.totvs.tjf.api.jpa.repository.impl.ApiJpaRepositoryImpl;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class)
@EnableSwagger2
public class StarWarsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsServicesApplication.class, args);
	}

}
