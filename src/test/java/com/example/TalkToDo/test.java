package com.example.TalkToDo;

import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.ScheduleScope;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.UserRepository;
import com.example.TalkToDo.repository.TodoRepository;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
public class test {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private TodoRepository todoRepository;

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

  @Test
  public void CreateDummyTodoAndSchedule() {
    // 할 일 상태
    String[] todoStatus = { "진행중", "완료" };
    
    // 일정 카테고리 (type과 category는 동일한 값 사용)
    String[] scheduleTypes = { "COMPANY", "TEAM", "PERSONAL" };
    String[] locations = { "회의실A", "회의실B", "사무실", "외부" };
    String[] colors = { "#FF5733", "#33FF57", "#3357FF", "#F3FF33" };

    // 사용자 생성
    String[] names = { "김민준", "이서연", "박지후", "최예린", "정하늘", "오지민", "한도윤", "유하람", "신유진", "장서준" };
    String[] department = { "개발부", "인사팀", "영업팀", "마케팅팀", "경영팀" };
    String[] position = { "사원", "대리", "과장", "부장", "임원" };

    List<User> users = new ArrayList<>();
    
    // 사용자 생성 및 저장
    for (int i = 0; i < 10; i++) {
      User user = new User();
      user.setName(names[i]);
      user.setEmail("user" + i + "@test.com");
      user.setDepartment(department[i % department.length]);
      user.setPosition(position[i % position.length]);
      user.setUsername("user" + i);
      user.setPassword(passwordEncoder.encode("1234"));
      userRepository.save(user);
      users.add(user);
    }
    
    // 각 사용자별 할 일과 일정 생성
    for (User user : users) {
      // 각 사용자별 할 일 생성
      for (int j = 0; j < 5; j++) {
        boolean isSchedule = j % 2 == 0; // 짝수번째 할 일은 일정에 표시
        
        Todo todo = Todo.builder()
            .title(user.getName() + "의 할 일 " + (j + 1))
            .type("TODO")
            .startDate(LocalDate.now())
            .dueDate(LocalDate.now().plusDays(j + 1))
            .assignee(user)
            .status(todoStatus[j % todoStatus.length])
            .isSchedule(isSchedule)
            .build();
        todoRepository.save(todo);

        // 일정 생성
        String scheduleType = scheduleTypes[j % scheduleTypes.length];
        Schedule schedule = Schedule.builder()
            .user(user)
            .title(user.getName() + "의 일정 " + (j + 1))
            .type(scheduleType)
            .startDate(LocalDate.now().plusDays(j))
            .endDate(LocalDate.now().plusDays(j + 1))
            .category(scheduleType)
            .displayInCalendar(true)
            .scope(scheduleType.equals("COMPANY") ? ScheduleScope.COMPANY : 
                   scheduleType.equals("TEAM") ? ScheduleScope.TEAM : 
                   ScheduleScope.PERSONAL)
            .description("테스트 일정입니다.")
            .startTime(LocalTime.of(9 + j, 0))  // 9시부터 시작해서 1시간씩 증가
            .endTime(LocalTime.of(10 + j, 0))   // 10시부터 시작해서 1시간씩 증가
            .location(locations[j % locations.length])
            .color(colors[j % colors.length])
            .isTodo(false)
            .build();
        scheduleRepository.save(schedule);
      }
    }
    System.out.println("사용자, 할 일, 일정 더미 데이터 생성 완료");
  }

  @Test
  public void DeleteAllData() {
    // 순서 중요: 외래 키 제약조건 때문에 먼저 자식 테이블부터 삭제
    todoRepository.deleteAll();
    scheduleRepository.deleteAll();
    userRepository.deleteAll();
    
    System.out.println("모든 데이터 삭제 완료");
  }
}
