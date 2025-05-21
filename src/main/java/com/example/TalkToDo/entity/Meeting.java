package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meetings")
public class Meeting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String audioUrl;

    @OneToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

    private boolean favorite;

    @ManyToMany
    @JoinTable(
        name = "meeting_schedules",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedules;

    // createdAt은 BaseTimeEntity에서 상속
} 