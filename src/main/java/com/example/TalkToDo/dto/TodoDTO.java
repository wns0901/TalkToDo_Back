package com.example.TalkToDo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoDTO {
    private Long id;
    private String text;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String assignee;
    private String status;
    private boolean schedule;
} 