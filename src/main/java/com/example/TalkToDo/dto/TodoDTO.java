package com.example.TalkToDo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TodoDTO {
    private Long id;
    private String text;
    private String type;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String assignee;
    private String status;
    private boolean schedule;
} 