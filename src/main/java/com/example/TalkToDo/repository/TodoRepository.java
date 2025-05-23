package com.example.TalkToDo.repository;

import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByMeeting(Meeting meeting);
    List<Todo> findByAssignee(User user);
    List<Todo> findByStatus(String status);
} 