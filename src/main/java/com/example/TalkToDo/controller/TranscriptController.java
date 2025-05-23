package com.example.TalkToDo.controller;

import com.example.TalkToDo.entity.Transcript;
import com.example.TalkToDo.service.TranscriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transcripts")
public class TranscriptController {

    @Autowired
    private TranscriptService transcriptService;

    @GetMapping
    public List<Transcript> getAllTranscripts() {
        return transcriptService.getAllTranscripts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transcript> getTranscriptById(@PathVariable Long id) {
        return transcriptService.getTranscriptById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transcript createTranscript(@RequestBody Transcript transcript) {
        return transcriptService.createTranscript(transcript);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transcript> updateTranscript(@PathVariable Long id, @RequestBody Transcript transcriptDetails) {
        return transcriptService.updateTranscript(id, transcriptDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTranscript(@PathVariable Long id) {
        return transcriptService.deleteTranscript(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
} 