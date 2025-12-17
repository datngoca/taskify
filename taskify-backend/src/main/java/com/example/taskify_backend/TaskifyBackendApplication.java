package com.example.taskify_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskifyBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskifyBackendApplication.class, args);
	}

}
