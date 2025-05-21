package com.example.TalkToDo.controller;

import com.example.TalkToDo.entity.MeetingNote;
import com.example.TalkToDo.service.MeetingNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-notes")
public class MeetingNoteController {

    @Autowired
    private MeetingNoteService meetingNoteService;

    @GetMapping
    public List<MeetingNote> getAllMeetingNotes() {
        return meetingNoteService.getAllMeetingNotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingNote> getMeetingNoteById(@PathVariable Long id) {
        return meetingNoteService.getMeetingNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<MeetingNote>> getMeetingNotesByMeeting(@PathVariable Long meetingId) {
        return meetingNoteService.getMeetingNotesByMeeting(meetingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MeetingNote createMeetingNote(@RequestBody MeetingNote meetingNote) {
        return meetingNoteService.createMeetingNote(meetingNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeetingNote> updateMeetingNote(@PathVariable Long id, @RequestBody MeetingNote meetingNoteDetails) {
        return meetingNoteService.updateMeetingNote(id, meetingNoteDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeetingNote(@PathVariable Long id) {
        return meetingNoteService.deleteMeetingNote(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
} 