package com.example.TalkToDo.controller;

import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

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

    @PostMapping("/{todoId}/add-to-calendar")
    public ResponseEntity<ScheduleDTO> addTodoToCalendar(@PathVariable Long todoId) {
        return ResponseEntity.ok(todoService.addTodoToCalendar(todoId));
    }
} 