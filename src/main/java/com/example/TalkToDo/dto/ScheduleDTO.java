package com.example.TalkToDo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.TalkToDo.entity.ScheduleScope;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ScheduleDTO {
    private Long id;
    private String userId;
    private String title;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private String category;
    private String type;
    private boolean displayInCalendar;
    private boolean isTodo;
    private Long originalTodoId;
    private ScheduleScope scope;
    private String description;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    
    private String userName;
    private String location;
    private String color;

    public boolean isTodo() {
        return isTodo;
    }

    public void setIsTodo(boolean isTodo) {
        this.isTodo = isTodo;
    }
    private Long meetingId;
} 