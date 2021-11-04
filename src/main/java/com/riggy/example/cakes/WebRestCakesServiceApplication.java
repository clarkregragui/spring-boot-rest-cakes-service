package com.riggy.example.cakes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication
public class WebRestCakesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRestCakesServiceApplication.class, args);
	}

}
