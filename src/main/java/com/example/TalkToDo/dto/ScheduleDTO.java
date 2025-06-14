package com.example.TalkToDo.dto;

import com.example.TalkToDo.entity.ScheduleScope;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDTO {
    private Long id;
    private String userId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String category;
    private String type;
    private boolean displayInCalendar;
    private boolean isTodo;
    private Long originalTodoId;
    private ScheduleScope scope;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userName;
    private String location;
    private String color;
    private Long meetingId;
} 