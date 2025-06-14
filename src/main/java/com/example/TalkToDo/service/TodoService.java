package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.dto.TodoDTO;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.UserRepository;
import com.example.TalkToDo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

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

    // Todo 엔티티를 DTO로 변환
    public TodoDTO convertToDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setType(todo.getType());
        dto.setStartDate(todo.getStartDate());
        dto.setDueDate(todo.getDueDate());
        dto.setStatus(todo.getStatus());
        dto.setSchedule(todo.isSchedule());
        
        if (todo.getAssignee() != null) {
            dto.setAssigneeId(todo.getAssignee().getId().toString());
            dto.setAssigneeName(todo.getAssignee().getName());
        }
        
        if (todo.getMeeting() != null) {
            dto.setMeetingId(todo.getMeeting().getId());
        }
        
        return dto;
    }

    // DTO를 Todo 엔티티로 변환
    public Todo convertToEntity(TodoDTO dto) {
        Todo todo = new Todo();
        todo.setId(dto.getId());
        todo.setTitle(dto.getTitle());
        todo.setType(dto.getType());
        todo.setStartDate(dto.getStartDate());
        todo.setDueDate(dto.getDueDate());
        todo.setStatus(dto.getStatus());
        
        if (dto.getAssigneeId() != null) {
            User assignee = userRepository.findById(Long.parseLong(dto.getAssigneeId()))
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            todo.setAssignee(assignee);
        }
        
        if (dto.getMeetingId() != null) {
            Meeting meeting = meetingRepository.findById(dto.getMeetingId())
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));
            todo.setMeeting(meeting);
        }
        
        return todo;
    }

    @Transactional
    public Optional<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    existingTodo.setTitle(todoDetails.getTitle());
                    existingTodo.setDueDate(todoDetails.getDueDate());
                    return todoRepository.save(existingTodo);
                });
    }

    @Transactional
    public boolean deleteTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.delete(todo);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Todo addTodoToCalendar(Long todoId, Long userId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        todo.setAddedToMyTodo(true);
        
        User user = User.builder().id(userId).build();
        todo.setAssignee(user);

        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> getActiveTodos(Long userId) {
        return todoRepository.findByAssigneeIdAndStatusNot(userId, "COMPLETED");
    }

    @Transactional(readOnly = true)
    public List<Todo> getCompletedTodos(Long userId) {
        return todoRepository.findByAssigneeIdAndStatus(userId, "COMPLETED");
    }

    @Transactional
    public Todo toggleTodoComplete(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        if ("COMPLETED".equals(todo.getStatus())) {
            todo.setStatus("IN_PROGRESS");
            todo.setCompletedAt(null);
        } else {
            todo.setStatus("COMPLETED");
            todo.setCompletedAt(LocalDateTime.now());
        }
        
        return todoRepository.save(todo);
    }

    @Transactional
    public Todo restoreTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        todo.setStatus("IN_PROGRESS");
        todo.setCompletedAt(null);
        
        return todoRepository.save(todo);
    }
} 