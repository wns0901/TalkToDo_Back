package com.example.TalkToDo;

import com.example.TalkToDo.entity.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.TalkToDo.repository.UserRepository;


@SpringBootTest
public class test {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void test1() {
    User user = new User();
    user.setName("test");
    user.setEmail("test@test.com");
    user.setDepartment("test");
    user.setPosition("test");
    user.setUsername("test");
    user.setPassword(passwordEncoder.encode("1234"));

    userRepository.save(user);

    User user2 = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
    System.out.println(user2.getName());
    System.out.println("테스트 성공");
  }
}
