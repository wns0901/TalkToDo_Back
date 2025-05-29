package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public List<String> getUserCategories(Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);
        return schedules.stream()
                .map(Schedule::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public String addUserCategory(Long userId, String category) {
        // 카테고리가 이미 존재하는지 확인
        List<String> existingCategories = getUserCategories(userId);
        if (existingCategories.contains(category)) {
            throw new RuntimeException("Category already exists");
        }
        return category;
    }

    @Transactional
    public void deleteUserCategory(Long userId, String category) {
        // 해당 카테고리를 사용하는 일정이 있는지 확인
        List<Schedule> schedules = scheduleRepository.findByUserIdAndCategory(userId, category);
        if (!schedules.isEmpty()) {
            throw new RuntimeException("Cannot delete category that is in use");
        }
    }
} 