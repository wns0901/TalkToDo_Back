package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import com.example.TalkToDo.converter.LocalTimeConverter;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedules")
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
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private String category;
    private boolean displayInCalendar;
    @Column(nullable = false)
    @Builder.Default
    private boolean addedToMySchedule = false;

    @Column(name = "is_todo")
    private boolean isTodo;

    @Column(name = "original_todo_id")
    private Long originalTodoId;

    @Enumerated(EnumType.STRING)
    private ScheduleScope scope;  // COMPANY, TEAM, PERSONAL

    private String description;

    @Convert(converter = LocalTimeConverter.class)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Convert(converter = LocalTimeConverter.class)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private String location;

    private String color;

    public String getUserId() {
        return user != null ? user.getId().toString() : null;
    }

    public void setUserId(String userId) {
        if (this.user == null) this.user = new User();
        this.user.setId(Long.parseLong(userId));
    }

    public boolean isTodo() {
        return isTodo;
    }

    public void setIsTodo(boolean isTodo) {
        this.isTodo = isTodo;
    }

    public Long getOriginalTodoId() {
        return originalTodoId;
    }

    public void setOriginalTodoId(Long originalTodoId) {
        this.originalTodoId = originalTodoId;
    }
} 