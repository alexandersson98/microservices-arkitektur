package com.example.boilerroom_labb1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BoilerroomLabb1Application {

	public static void main(String[] args) {
		SpringApplication.run(BoilerroomLabb1Application.class, args);
	}

}
