package com.example.TalkToDo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ScheduleDTO {
    private Long id;
    private String userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;
    private String type;
    private boolean displayInCalendar;
    private boolean isTodo;
    private Long originalTodoId;
} 