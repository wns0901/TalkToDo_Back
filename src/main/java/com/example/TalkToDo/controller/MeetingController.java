package com.example.TalkToDo.controller;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.dto.MeetingDTO;
import com.example.TalkToDo.dto.MeetingNotesDTO;
import com.example.TalkToDo.dto.TranscriptLineDTO;
import com.example.TalkToDo.service.MeetingService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingDTO> getMeetingDetails(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getMeetingDetails(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Meeting>> getMeetingsByUser(@PathVariable Long userId) {
        return meetingService.getMeetingsByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Meeting createMeeting(
            @RequestPart("audioFile") MultipartFile audioFile) {
        return meetingService.createMeeting(audioFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long id, @RequestBody Meeting meetingDetails) {
        return meetingService.updateMeeting(id, meetingDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return meetingService.deleteMeeting(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // 회의록 수정
    @PutMapping("/{meetingId}/notes")
    public ResponseEntity<MeetingNotesDTO> updateMeetingNotes(
            @PathVariable Long meetingId,
            @RequestBody MeetingNotesDTO notesDTO) {
        return ResponseEntity.ok(meetingService.updateMeetingNotes(meetingId, notesDTO));
    }

    // 회의록 텍스트 수정
    @PutMapping("/{meetingId}/transcript")
    public ResponseEntity<List<TranscriptLineDTO>> updateTranscript(
            @PathVariable Long meetingId,
            @RequestBody List<TranscriptLineDTO> transcriptLines) {
        return ResponseEntity.ok(meetingService.updateTranscript(meetingId, transcriptLines));
    }
}