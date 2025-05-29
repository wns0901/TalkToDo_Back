package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todos")
public class Todo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String title;

    private String type;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private String status;

    @Column(nullable = false)
    @Builder.Default
    private boolean addedToMyTodo = false;
    
    @Column(nullable = false)
    @Builder.Default
    private boolean isSchedule = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
} 