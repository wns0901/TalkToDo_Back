package com.example.TalkToDo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.TalkToDo.repository.UserRepository;
import com.example.TalkToDo.entity.User;

@SpringBootTest
class TalkToDoApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		
	}

	@Test
	void test2() {
		User user = new User();
		user.setName("test");
		user.setEmail("test@test.com");
		user.setDepartment("test");
		user.setPosition("test");
		user.setUsername("test");
		user.setRole("test");
		userRepository.save(user);

		User user2 = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
		System.out.println(user2.getName());
	}

}
