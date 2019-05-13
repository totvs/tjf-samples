package com.tjf.sample.github.securityweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tjf.sample.github.securityweb.component.MachineManager;

@SpringBootApplication
public class SecurityWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SecurityWebApplication.class, args);
	}
}
