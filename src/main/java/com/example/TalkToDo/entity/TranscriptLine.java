package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transcript_lines")
public class TranscriptLine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private double startTime;

    private double endTime;

    private String speaker;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
} 