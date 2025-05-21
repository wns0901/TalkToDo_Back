package com.example.TalkToDo.repository;

import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser(User user);
    List<Schedule> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
    List<Schedule> findByAddedToMyScheduleTrue();
} 