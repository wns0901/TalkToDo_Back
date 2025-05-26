package com.example.TalkToDo.service;

import com.example.TalkToDo.dto.ScheduleDTO;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    // 일정 생성
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = convertToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }

    // 일정 수정
    @Transactional
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        updateScheduleFromDTO(existingSchedule, scheduleDTO);
        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        return convertToDTO(updatedSchedule);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    // 사용자의 모든 일정 조회
    public List<ScheduleDTO> getAllSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 기간별 일정 조회
    public List<ScheduleDTO> getSchedulesByDateRange(Long userId, LocalDate start, LocalDate end) {
        return scheduleRepository.findByUserIdAndStartDateBetween(userId, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 카테고리별 일정 조회
    public List<ScheduleDTO> getSchedulesByCategory(Long userId, String category) {
        return scheduleRepository.findByUserIdAndCategory(userId, category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 할일 목록 조회
    public List<ScheduleDTO> getTodosByUserId(Long userId) {
        return scheduleRepository.findByUserIdAndType(userId, "TODO")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 날짜별 일정 조회
    public List<ScheduleDTO> getSchedulesByDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return scheduleRepository
            .findByUserAndDisplayInCalendarIsTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user, date, date)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // DTO를 Entity로 변환
    private Schedule convertToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setUserId(dto.getUserId());
        schedule.setTitle(dto.getTitle());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setCategory(dto.getCategory());
        schedule.setType(dto.getType());
        schedule.setDisplayInCalendar(dto.isDisplayInCalendar());
        return schedule;
    }

    // Entity를 DTO로 변환
    public ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setTitle(schedule.getTitle());
        dto.setStartDate(schedule.getStartDate());
        dto.setEndDate(schedule.getEndDate());
        dto.setCategory(schedule.getCategory());
        dto.setType(schedule.getType());
        dto.setDisplayInCalendar(schedule.isDisplayInCalendar());
        return dto;
    }

    // DTO로부터 Entity 업데이트
    private void updateScheduleFromDTO(Schedule schedule, ScheduleDTO dto) {
        schedule.setTitle(dto.getTitle());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setCategory(dto.getCategory());
        schedule.setType(dto.getType());
        schedule.setDisplayInCalendar(dto.isDisplayInCalendar());
    }

    @Transactional
    public void addScheduleToUser(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("일정 없음"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));
        schedule.setUser(user);
        scheduleRepository.save(schedule);
    }
} 