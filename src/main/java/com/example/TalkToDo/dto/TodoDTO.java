package com.example.TalkToDo.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TodoDTO {
    private Long id;
    private String title;
    private String type;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String assigneeId;
    private String assigneeName;
    private String status;
    private Long meetingId;
    private boolean isSchedule;
} 