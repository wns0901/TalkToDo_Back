package com.example.TalkToDo.service;

import com.example.TalkToDo.dto.ScheduleDTO;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.ScheduleScope;
import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    // 모든 일정 조회
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 특정 일정 조회
    public ScheduleDTO getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
    }

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
        User user = User.builder().id(userId).build();
        return scheduleRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 기간별 일정 조회
    public List<ScheduleDTO> getSchedulesByDateRange(Long userId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();
        return scheduleRepository.findByUserAndStartDateBetween(user, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 카테고리별 일정 조회
    public List<ScheduleDTO> getSchedulesByCategory(Long userId, String category) {
        User user = User.builder().id(userId).build();
        return scheduleRepository.findByUserAndCategory(user, category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 할일 목록 조회
    public List<ScheduleDTO> getTodosByUserId(Long userId) {
        User user = User.builder().id(userId).build();
        return scheduleRepository.findByUserAndType(user, "TODO")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 날짜별 일정 조회 (통합된 메서드)
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedulesByDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return scheduleRepository
                .findByUserAndDisplayInCalendarIsTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user, date,
                        date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // DTO를 Entity로 변환
    public Schedule convertToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setTitle(dto.getTitle());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setCategory(dto.getCategory());
        schedule.setType(dto.getType());
        schedule.setDisplayInCalendar(dto.isDisplayInCalendar());
        schedule.setIsTodo(dto.isTodo());
        schedule.setOriginalTodoId(dto.getOriginalTodoId());
        schedule.setScope(dto.getScope());
        schedule.setDescription(dto.getDescription());
        schedule.setLocation(dto.getLocation());
        schedule.setColor(dto.getColor());

        // startTime과 endTime 직접 설정
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(Long.parseLong(dto.getUserId()))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            schedule.setUser(user);
        }
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
        dto.setIsTodo(schedule.isTodo());
        dto.setOriginalTodoId(schedule.getOriginalTodoId());
        dto.setScope(schedule.getScope());
        dto.setDescription(schedule.getDescription());
        dto.setLocation(schedule.getLocation());
        dto.setColor(schedule.getColor());

        // startTime과 endTime 직접 설정
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());

        if (schedule.getUser() != null) {
            dto.setUserId(schedule.getUser().getId().toString());
            dto.setUserName(schedule.getUser().getName());
        }
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
        schedule.setLocation(dto.getLocation());
        schedule.setDescription(dto.getDescription());
        schedule.setColor(dto.getColor());
        schedule.setScope(dto.getScope());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setIsTodo(dto.isTodo());
        schedule.setOriginalTodoId(dto.getOriginalTodoId());
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

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByMonth(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        User user = User.builder().id(userId).build();
        return scheduleRepository.findByUserAndDisplayInCalendarIsTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(user, startDate, endDate);
    }

    @Transactional
    public Schedule addTodoToCalendar(Long todoId, Schedule schedule) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        // Todo의 isSchedule 필드를 true로 설정
        todo.setSchedule(true);
        todoRepository.save(todo);

        // 새로운 Schedule 객체 생성
        Schedule newSchedule = new Schedule();
        newSchedule.setTitle(todo.getTitle());
        newSchedule.setCategory("TODO");
        newSchedule.setType("TODO");
        newSchedule.setIsTodo(true);
        newSchedule.setOriginalTodoId(todoId);
        newSchedule.setDisplayInCalendar(true);
        
        // Todo의 정보를 사용하여 일정 정보 설정
        newSchedule.setStartDate(todo.getStartDate());
        newSchedule.setEndDate(todo.getDueDate());
        newSchedule.setUser(todo.getAssignee());
        newSchedule.setScope(ScheduleScope.PERSONAL);  // 기본값으로 PERSONAL 설정
        
        // Todo의 상태에 따라 색상 설정
        if ("COMPLETED".equals(todo.getStatus())) {
            newSchedule.setColor("#808080");  // 완료된 경우 회색
        } else {
            newSchedule.setColor("#FF0000");  // 미완료된 경우 빨간색
        }

        // 일정 저장 및 반환
        return scheduleRepository.save(newSchedule);
    }

    @Transactional
    public void removeTodoFromCalendar(Long todoId) {
        // Todo의 isSchedule 필드를 false로 변경
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setSchedule(false);
        todoRepository.save(todo);
        
        // 일정 삭제
        scheduleRepository.deleteByOriginalTodoId(todoId);
    }

    @Transactional
    public List<Schedule> getSchedulesByMeetingId(Long meetingId) {
        Meeting meeting = Meeting.builder().id(meetingId).build();
        return scheduleRepository.findByMeeting(meeting);
    }

    @Transactional
    public Schedule updateScheduleInMeeting(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setTitle(scheduleDTO.getTitle());
        schedule.setStartDate(scheduleDTO.getStartDate());
        schedule.setEndDate(scheduleDTO.getEndDate());
        schedule.setScope(scheduleDTO.getScope());
        schedule.setDescription(scheduleDTO.getDescription());
        schedule.setLocation(scheduleDTO.getLocation());

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule addToMySchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
                
        schedule.setAddedToMySchedule(true);
        User user = User.builder().id(userId).build();
        schedule.setDisplayInCalendar(true);
        schedule.setUser(user);
        
        return scheduleRepository.save(schedule);
    }
}