package com.example.TalkToDo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.talktodo")
@EnableJpaRepositories(basePackages = "com.talktodo.repository")
@EntityScan(basePackages = "com.talktodo.entity")
@EnableJpaAuditing
public class TalkToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkToDoApplication.class, args);
	}

}
