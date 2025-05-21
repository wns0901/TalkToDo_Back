package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Optional<List<Schedule>> getSchedulesByUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> scheduleRepository.findByUser(user));
    }

    public List<Schedule> getSchedulesByPeriod(LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByStartDateBetween(start, end);
    }

    public List<Schedule> getMySchedules() {
        return scheduleRepository.findByAddedToMyScheduleTrue();
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Optional<Schedule> updateSchedule(Long id, Schedule scheduleDetails) {
        return scheduleRepository.findById(id)
                .map(existingSchedule -> {
                    scheduleDetails.setId(id);
                    return scheduleRepository.save(scheduleDetails);
                });
    }

    public boolean deleteSchedule(Long id) {
        return scheduleRepository.findById(id)
                .map(schedule -> {
                    scheduleRepository.delete(schedule);
                    return true;
                })
                .orElse(false);
    }
} 