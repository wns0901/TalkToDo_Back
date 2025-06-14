package com.example.TalkToDo.dto;

import java.util.List;

import lombok.Data;

@Data
public class EmailDTO {
  private List<String> toList;
  private String subject;
  private String text;
  private String from;
  private Long meetingId;
}
