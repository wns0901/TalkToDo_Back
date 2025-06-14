package com.example.TalkToDo.controller;

import com.example.TalkToDo.dto.TodoDTO;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.TalkToDo.config.security.PrincipalDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<TodoDTO> getAllTodos() {
        return todoService.getAllTodos().stream()
                .map(todoService::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(todoService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<TodoDTO>> getTodosByMeeting(@PathVariable Long meetingId) {
        return todoService.getTodosByMeeting(meetingId)
                .map(todos -> ResponseEntity.ok(todos.stream()
                        .map(todoService::convertToDTO)
                        .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<TodoDTO>> getTodosByAssignee(@PathVariable Long userId) {
        return todoService.getTodosByAssignee(userId)
                .map(todos -> ResponseEntity.ok(todos.stream()
                        .map(todoService::convertToDTO)
                        .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<TodoDTO> getTodosByStatus(@PathVariable String status) {
        return todoService.getTodosByStatus(status).stream()
                .map(todoService::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public TodoDTO createTodo(@RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.convertToEntity(todoDTO);
        Todo savedTodo = todoService.createTodo(todo);
        return todoService.convertToDTO(savedTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable Long id, @RequestBody TodoDTO todoDTO) {
        Todo todo = todoService.convertToEntity(todoDTO);
        return todoService.updateTodo(id, todo)
                .map(todoService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // @PostMapping("/{todoId}/add-to-calendar")
    // public ResponseEntity<ScheduleDTO> addTodoToCalendar(@PathVariable Long todoId) {
    //     return ResponseEntity.ok(todoService.addTodoToCalendar(todoId));
    // }

    // 활성 할일 조회
    @GetMapping("/user/{userId}/active")
    public List<TodoDTO> getActiveTodos(@PathVariable Long userId) {
        return todoService.getActiveTodos(userId).stream()
                .map(todoService::convertToDTO)
                .collect(Collectors.toList());
    }

    // 완료된 할일 조회
    @GetMapping("/user/{userId}/completed")
    public List<TodoDTO> getCompletedTodos(@PathVariable Long userId) {
        return todoService.getCompletedTodos(userId).stream()
                .map(todoService::convertToDTO)
                .collect(Collectors.toList());
    }

    // 할일 완료 상태 토글
    @PutMapping("/{id}/complete")
    public TodoDTO toggleTodoComplete(@PathVariable Long id) {
        Todo todo = todoService.toggleTodoComplete(id);
        return todoService.convertToDTO(todo);
    }

    // 할일 복구
    @PutMapping("/{id}/restore")
    public TodoDTO restoreTodo(@PathVariable Long id) {
        Todo todo = todoService.restoreTodo(id);
        return todoService.convertToDTO(todo);
    }

    // 사용자의 모든 할일 조회
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TodoDTO>> getTodosByUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        logger.info("Request to get todos for user {} by user {}", 
            userId, principalDetails.getUser().getId());
            
        // 현재 로그인한 사용자와 요청한 userId가 일치하는지 확인
        if (principalDetails == null || principalDetails.getUser() == null) {
            logger.warn("Access denied: No authenticated user");
            return ResponseEntity.status(403).build();
        }

        if (!principalDetails.getUser().getId().equals(userId)) {
            logger.warn("Access denied: User {} tried to access todos for user {}", 
                principalDetails.getUser().getId(), userId);
            return ResponseEntity.status(403).build();
        }

        try {
            return todoService.getTodosByAssignee(userId)
                    .map(todos -> ResponseEntity.ok(todos.stream()
                            .map(todoService::convertToDTO)
                            .collect(Collectors.toList())))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error getting todos for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
} 