package com.example.TalkToDo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TalkToDo.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUser_Id(Long userId);
    List<Schedule> findByUser_IdAndStartDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Schedule> findByUser_IdAndCategory(Long userId, String category);
    List<Schedule> findByUser_IdAndIsTodo(Long userId, boolean isTodo);
} 