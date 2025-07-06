package com.tutorXpert.tutorxpert_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tutorXpert.tutorxpert_backend.mapper")

public class TutorxpertBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorxpertBackendApplication.class, args);
	}
}
