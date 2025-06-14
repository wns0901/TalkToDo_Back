package com.example.TalkToDo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TalkToDo.entity.Chat;
import com.example.TalkToDo.entity.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
  List<Chat> findByUser(User user);
}
