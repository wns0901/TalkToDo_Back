package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.UserRepository;
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
} 