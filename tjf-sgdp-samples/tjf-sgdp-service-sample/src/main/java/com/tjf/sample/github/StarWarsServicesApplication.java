package com.tjf.sample.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories()
public class StarWarsServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsServicesApplication.class, args);
	}

}
