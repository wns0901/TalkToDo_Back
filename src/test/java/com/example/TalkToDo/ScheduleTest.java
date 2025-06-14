package com.example.TalkToDo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.TalkToDo.dto.ScheduleDTO;
import com.example.TalkToDo.service.ScheduleService;

@SpringBootTest
public class ScheduleTest {
  @Autowired
  private ScheduleService scheduleService;

  @Test
  public void testAddToMySchedule() {
    List<ScheduleDTO> schedules = scheduleService.getAllSchedulesByUserId(1L);
    System.out.println("Test:=====================================================");
    schedules.forEach(System.out::println);
  }
}
