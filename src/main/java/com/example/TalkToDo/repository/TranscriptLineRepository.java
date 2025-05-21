package com.example.TalkToDo.repository;

import com.example.TalkToDo.entity.TranscriptLine;
import com.example.TalkToDo.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TranscriptLineRepository extends JpaRepository<TranscriptLine, Long> {
    List<TranscriptLine> findByTranscript(Transcript transcript);
} 