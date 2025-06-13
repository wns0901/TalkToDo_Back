package com.example.TalkToDo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TalkToDo.dto.EmailDTO;
import com.example.TalkToDo.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;

  @PostMapping("/send")
  public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
    try {
      emailService.sendEmail(emailDTO);
      return ResponseEntity.ok("Email sent successfully");
    } catch (MessagingException | IOException e) {
      return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
    }
  }
}
