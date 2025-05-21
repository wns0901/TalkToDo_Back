package com.example.TalkToDo.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transcript_lines")
public class TranscriptLine extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String timestamp;
    private String speaker;
    private String content;

    @ManyToOne
    @JoinColumn(name = "transcript_id")
    private Transcript transcript;

} 