package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
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

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String audioFileUrl;
    private String transcriptFileUrl;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private String audioUrl;

    @OneToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

    private boolean favorite;

    @OneToMany
    @JoinTable(
        name = "meeting_schedules",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedules;

    // createdAt은 BaseTimeEntity에서 상속
} 