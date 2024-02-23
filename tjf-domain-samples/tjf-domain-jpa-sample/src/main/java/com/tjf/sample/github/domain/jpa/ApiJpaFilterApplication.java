package com.tjf.sample.github.domain.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories()
public class ApiJpaFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiJpaFilterApplication.class, args);
	}

}
