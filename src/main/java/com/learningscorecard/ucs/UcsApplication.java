package com.learningscorecard.ucs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UcsApplication {

	public static void main(String[] args) {
		SpringApplication.run(UcsApplication.class, args);
	}

}
