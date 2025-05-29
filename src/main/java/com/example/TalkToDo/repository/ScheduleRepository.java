package com.example.TalkToDo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(Long userId);
    List<Schedule> findByUserIdAndStartDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Schedule> findByUserIdAndStartDate(Long userId, LocalDate date);
    List<Schedule> findByUserIdAndCategory(Long userId, String category);
    List<Schedule> findByUserIdAndType(Long userId, String type);
    List<Schedule> findByUserAndDisplayInCalendarIsTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(User user, LocalDate date1, LocalDate date2);
    void deleteByOriginalTodoId(Long todoId);
} 