package com.tjf.sample.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.totvs.tjf.core.utils.OSGIApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SwQueryApplication extends OSGIApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwQueryApplication.class, args);
	}

}