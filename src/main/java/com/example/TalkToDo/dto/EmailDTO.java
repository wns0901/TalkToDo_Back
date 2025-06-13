package com.example.TalkToDo.dto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class EmailDTO {
  private String to;
  private String subject;
  private String text;
  private String from;
  private List<MultipartFile> attachments;
}
