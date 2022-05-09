package com.tjf.samples.treports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@EnableJpaRepositories()
public class TreportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreportsApplication.class, args);
	}
}