package com.example.TalkToDo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.User;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser(User user);

    List<Schedule> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Schedule> findByUserAndStartDate(User user, LocalDate date);

    List<Schedule> findByUserAndCategory(User user, String category);

    List<Schedule> findByUserAndType(User user, String type);

    List<Schedule> findByUserAndDisplayInCalendarIsTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(User user,
            LocalDate startDate, LocalDate endDate);

    void deleteByOriginalTodoId(Long todoId);

    List<Schedule> findByMeeting(Meeting meeting);
}