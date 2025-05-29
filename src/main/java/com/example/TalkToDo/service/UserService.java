package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.dto.MyPageDTO;
import com.example.TalkToDo.repository.UserRepository;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userDetails.setId(id);
                    return userRepository.save(userDetails);
                });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<MyPageDTO> getMyPageData(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    MyPageDTO myPageDTO = new MyPageDTO();
                    myPageDTO.setUser(user);
                    
                    // 미팅 데이터
                    List<Meeting> meetings = meetingRepository.findByCreatedBy(user);
                    myPageDTO.setMeetings(meetings);
                    myPageDTO.setTotalMeetings(meetings.size());
                    
                    // 할일 데이터
                    List<Todo> todos = todoRepository.findByAssignee(user);
                    myPageDTO.setTodos(todos);
                    myPageDTO.setTotalTodos(todos.size());
                    myPageDTO.setCompletedTodos(todos.stream()
                            .filter(todo -> "COMPLETED".equals(todo.getStatus()))
                            .count());
                    
                    // 일정 데이터
                    List<Schedule> schedules = scheduleRepository.findByUserId(userId);
                    myPageDTO.setSchedules(schedules);
                    myPageDTO.setTotalSchedules(schedules.size());
                    
                    return myPageDTO;
                });
    }
} 