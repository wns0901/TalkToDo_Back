package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Transcript;
import com.example.TalkToDo.repository.TranscriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TranscriptService {

    @Autowired
    private TranscriptRepository transcriptRepository;

    public List<Transcript> getAllTranscripts() {
        return transcriptRepository.findAll();
    }

    public Optional<Transcript> getTranscriptById(Long id) {
        return transcriptRepository.findById(id);
    }

    public Transcript createTranscript(Transcript transcript) {
        return transcriptRepository.save(transcript);
    }

    public Optional<Transcript> updateTranscript(Long id, Transcript transcriptDetails) {
        return transcriptRepository.findById(id)
                .map(existingTranscript -> {
                    transcriptDetails.setId(id);
                    return transcriptRepository.save(transcriptDetails);
                });
    }

    public boolean deleteTranscript(Long id) {
        return transcriptRepository.findById(id)
                .map(transcript -> {
                    transcriptRepository.delete(transcript);
                    return true;
                })
                .orElse(false);
    }
} 