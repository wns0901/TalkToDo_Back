package com.example.TalkToDo.controller;

import com.example.TalkToDo.entity.TranscriptLine;
import com.example.TalkToDo.service.TranscriptLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transcript-lines")
public class TranscriptLineController {

    @Autowired
    private TranscriptLineService transcriptLineService;

    @GetMapping
    public List<TranscriptLine> getAllTranscriptLines() {
        return transcriptLineService.getAllTranscriptLines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranscriptLine> getTranscriptLineById(@PathVariable Long id) {
        return transcriptLineService.getTranscriptLineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/transcript/{transcriptId}")
    public ResponseEntity<List<TranscriptLine>> getTranscriptLinesByTranscript(@PathVariable Long transcriptId) {
        return transcriptLineService.getTranscriptLinesByTranscript(transcriptId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TranscriptLine createTranscriptLine(@RequestBody TranscriptLine transcriptLine) {
        return transcriptLineService.createTranscriptLine(transcriptLine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranscriptLine> updateTranscriptLine(@PathVariable Long id, @RequestBody TranscriptLine transcriptLineDetails) {
        return transcriptLineService.updateTranscriptLine(id, transcriptLineDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTranscriptLine(@PathVariable Long id) {
        return transcriptLineService.deleteTranscriptLine(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
} 