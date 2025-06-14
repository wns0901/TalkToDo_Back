package com.example.TalkToDo.controller;

import com.example.TalkToDo.dto.TranscriptLineListDTO;
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

    @GetMapping("/{meetingId}")
    public ResponseEntity<List<TranscriptLine>> getTranscriptLineById(@PathVariable Long meetingId) {
        return ResponseEntity.ok(transcriptLineService.getTranscriptLineById(meetingId));
    }

    @PostMapping
    public TranscriptLine createTranscriptLine(@RequestBody TranscriptLine transcriptLine) {
        return transcriptLineService.createTranscriptLine(transcriptLine);
    }

    @PutMapping
    public ResponseEntity<List<TranscriptLine>> updateTranscriptLine(@RequestBody TranscriptLineListDTO transcriptLineList) {
        return ResponseEntity.ok(transcriptLineService.updateTranscriptLine(transcriptLineList.getTranscriptLineList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTranscriptLine(@PathVariable Long id) {
        return transcriptLineService.deleteTranscriptLine(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
} 