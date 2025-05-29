package com.example.TalkToDo.dto;

import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.Schedule;
import lombok.Data;
import java.util.List;

@Data
public class MyPageDTO {
    private User user;
    private List<Meeting> meetings;
    private List<Todo> todos;
    private List<Schedule> schedules;
    
    // 통계 데이터
    private long totalMeetings;
    private long totalTodos;
    private long completedTodos;
    private long totalSchedules;
} 