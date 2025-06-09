package com.example.TalkToDo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TalkToDo.config.security.PrincipalDetails;
import com.example.TalkToDo.entity.User;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @GetMapping("")
  public User getUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      throw new RuntimeException("로그인 필요");
    }
    return principalDetails.getUser();
  }
}