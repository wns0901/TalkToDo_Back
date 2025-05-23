package com.example.TalkToDo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TalkToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkToDoApplication.class, args);
	}

}
