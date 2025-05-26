package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.dto.ScheduleDTO;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.TalkToDo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Optional<List<Todo>> getTodosByMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .map(meeting -> todoRepository.findByMeeting(meeting));
    }

    public Optional<List<Todo>> getTodosByAssignee(Long userId) {
        return userRepository.findById(userId)
                .map(user -> todoRepository.findByAssignee(user));
    }

    public List<Todo> getTodosByStatus(String status) {
        return todoRepository.findByStatus(status);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    todoDetails.setId(id);
                    return todoRepository.save(todoDetails);
                });
    }

    public boolean deleteTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.delete(todo);
                    return true;
                })
                .orElse(false);
    }

    // @Transactional
    // public ScheduleDTO addTodoToCalendar(Long todoId) {
    //     Todo todo = todoRepository.findById(todoId)
    //         .orElseThrow(() -> new RuntimeException("Todo not found"));
    //     // 이미 캘린더에 추가된 경우 중복 방지(원하면 추가)
    //     Schedule schedule = new Schedule();
    //     schedule.setTitle(todo.getTitle());
    //     schedule.setStartDate(java.time.LocalDate.now());
    //     schedule.setEndDate(java.time.LocalDate.now());
    //     schedule.setDisplayInCalendar(true);
    //     Schedule saved = scheduleRepository.save(schedule);
    //     // ScheduleService의 convertToDTO를 사용하려면 ScheduleService를 주입하거나, 여기서 직접 DTO 생성
    //     ScheduleDTO dto = new ScheduleDTO();
    //     dto.setId(saved.getId());
    //     dto.setTitle(saved.getTitle());
    //     dto.setStartDate(saved.getStartDate());
    //     dto.setEndDate(saved.getEndDate());
    //     dto.setCategory(saved.getCategory());
    //     dto.setType(saved.getType());
    //     dto.setUserId(saved.getUser().getId());
    //     dto.setCategoryDisplayName(saved.getCategoryDisplayName());
    //     dto.setTypeDisplayName(saved.getTypeDisplayName());
    //     dto.setCompleted(saved.isCompleted());
    //     dto.setFromMeeting(saved.getFromMeeting());
    //     dto.setTodo(saved.isTodo());
    //     dto.setDisplayInCalendar(saved.isDisplayInCalendar());
    //     dto.setOriginalTodoId(saved.getOriginalTodoId());
    //     return dto;
    // }
} 