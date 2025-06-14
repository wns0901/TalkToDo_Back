package com.example.TalkToDo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TalkToDo.config.security.PrincipalDetails;
import com.example.TalkToDo.dto.ChatDTO;
import com.example.TalkToDo.entity.Chat;
import com.example.TalkToDo.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @PostMapping
  public ResponseEntity<Chat> createChat(@RequestBody ChatDTO chatDTO) {
    Chat savedChat = chatService.save(chatDTO);
    return ResponseEntity.ok(savedChat);
  }

  @GetMapping
  public ResponseEntity<List<Chat>> getChats(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    Long userId = principalDetails.getUser().getId();
    List<Chat> chats = chatService.findByUser(userId);
    return ResponseEntity.ok(chats);
  }
}
