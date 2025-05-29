package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.TranscriptLine;
import com.example.TalkToDo.entity.Transcript;
import com.example.TalkToDo.repository.TranscriptLineRepository;
import com.example.TalkToDo.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TranscriptLineService {

    @Autowired
    private TranscriptLineRepository transcriptLineRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    public List<TranscriptLine> getAllTranscriptLines() {
        return transcriptLineRepository.findAll();
    }

    public Optional<TranscriptLine> getTranscriptLineById(Long id) {
        return transcriptLineRepository.findById(id);
    }

    public Optional<List<TranscriptLine>> getTranscriptLinesByTranscript(Long transcriptId) {
        return transcriptRepository.findById(transcriptId)
                .map(transcript -> transcriptLineRepository.findByTranscript(transcript));
    }

    public TranscriptLine createTranscriptLine(TranscriptLine transcriptLine) {
        return transcriptLineRepository.save(transcriptLine);
    }

    public Optional<TranscriptLine> updateTranscriptLine(Long id, TranscriptLine transcriptLineDetails) {
        return transcriptLineRepository.findById(id)
                .map(existingLine -> {
                    transcriptLineDetails.setId(id);
                    return transcriptLineRepository.save(transcriptLineDetails);
                });
    }

    @Transactional
    public boolean deleteTranscriptLine(Long id) {
        return transcriptLineRepository.findById(id)
                .map(line -> {
                    transcriptLineRepository.delete(line);
                    return true;
                })
                .orElse(false);
    }
} 