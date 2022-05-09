package com.tjf.sample.github.apicontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories()
public class ApiContextApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiContextApplication.class, args);
	}

}
