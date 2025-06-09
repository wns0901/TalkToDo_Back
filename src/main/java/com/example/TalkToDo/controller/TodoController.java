package com.example.TalkToDo.controller;


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

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<Todo>> getTodosByMeeting(@PathVariable Long meetingId) {
        return todoService.getTodosByMeeting(meetingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<Todo>> getTodosByAssignee(@PathVariable Long userId) {
        return todoService.getTodosByAssignee(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<Todo> getTodosByStatus(@PathVariable String status) {
        return todoService.getTodosByStatus(status);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        return todoService.updateTodo(id, todoDetails)
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
    public List<Todo> getActiveTodos(@PathVariable Long userId) {
        return todoService.getActiveTodos(userId);
    }

    // 완료된 할일 조회
    @GetMapping("/user/{userId}/completed")
    public List<Todo> getCompletedTodos(@PathVariable Long userId) {
        return todoService.getCompletedTodos(userId);
    }

    // 할일 완료 상태 토글
    @PutMapping("/{id}/complete")
    public Todo toggleTodoComplete(@PathVariable Long id) {
        return todoService.toggleTodoComplete(id);
    }

    // 할일 복구
    @PutMapping("/{id}/restore")
    public Todo restoreTodo(@PathVariable Long id) {
        return todoService.restoreTodo(id);
    }

    // 사용자의 모든 할일 조회
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Todo>> getTodosByUser(
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
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error getting todos for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
} 