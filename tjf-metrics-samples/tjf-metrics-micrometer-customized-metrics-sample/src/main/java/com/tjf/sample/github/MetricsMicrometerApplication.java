package com.tjf.sample.github;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfiguration
public class MetricsMicrometerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricsMicrometerApplication.class, args);
    }
}