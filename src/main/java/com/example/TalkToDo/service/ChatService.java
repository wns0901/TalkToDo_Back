package com.example.TalkToDo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.TalkToDo.dto.ChatDTO;
import com.example.TalkToDo.entity.Chat;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.ChatRepository;
import com.example.TalkToDo.util.Api;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final ChatRepository chatRepository;
  private final Api api;

  public Chat save(ChatDTO chatDTO) {
    Chat chat = Chat.builder()
        .user(User.builder().id(chatDTO.getUserId()).build())
        .message(chatDTO.getMessage())
        .isAi(false)
        .build();
    chat.setIsAi(false);

    String aiResponse = api.getAiResponse(chatDTO.getUserId(), chatDTO.getMessage());

    Chat aiChat = Chat.builder()
        .user(chat.getUser())
        .message(aiResponse)
        .isAi(true)
        .build();

    chatRepository.saveAll(List.of(chat, aiChat));

    return aiChat;
  }

  public List<Chat> findByUser(Long userId) {
    return chatRepository.findByUser(User.builder().id(userId).build());
  }
  
}
