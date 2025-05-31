package com.example.TalkToDo;

import com.example.TalkToDo.entity.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.UserRepository;
import com.example.TalkToDo.entity.Schedule;
import java.util.List;

@SpringBootTest
public class test {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

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

  @Test
  public void CreateDummyUser() {
    String[] names = { "김민준", "이서연", "박지후", "최예린", "정하늘", "오지민", "한도윤", "유하람", "신유진", "장서준" };

    String[] department = { "개발부", "인사팀", "영업팀", "마케팅팀", "경영팀" };
    String[] position = { "사원", "대리", "과장", "부장", "임원" };

    for (int i = 0; i < 10; i++) {
      User user = new User();
      user.setName(names[i]);
      user.setEmail("user" + i + "@test.com");
      user.setDepartment(department[i % department.length]);
      user.setPosition(position[i % position.length]);
      user.setUsername("user" + i);
      user.setPassword(passwordEncoder.encode("1234"));
      userRepository.save(user);
    }
  }

  @Test
  public void test2() {
    User user = User.builder()
      .id(1L)
      .build();

    List<Schedule> schedules = scheduleRepository.findByUser(user);
    System.out.println(schedules);
    System.out.println("테스트 성공-----------------------");
  }
}
