package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.example.TalkToDo.entity.BaseTimeEntity;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.User;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedules")
@EqualsAndHashCode(callSuper = true)
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String type;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;
    private boolean displayInCalendar;
    private boolean isTodo;
    private Long originalTodoId;

    @ManyToOne
    @JoinTable(
        name = "schedule_todos",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "todo_id")
    )
    private List<Todo> linkedTodos;

    private boolean addedToMySchedule;

    public String getUserId() {
        return user != null ? user.getId().toString() : null;
    }

    public void setUserId(String userId) {
        if (this.user == null) this.user = new User();
        this.user.setId(Long.parseLong(userId));
    }
} 