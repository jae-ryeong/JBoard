package com.example.JBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(JBoardApplication.class, args);
	}

}
