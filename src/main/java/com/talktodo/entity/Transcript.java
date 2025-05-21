package com.talktodo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transcripts")
public class Transcript extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "transcript")
    private Meeting meeting;

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL)
    private List<TranscriptLine> transcriptLines;

} 