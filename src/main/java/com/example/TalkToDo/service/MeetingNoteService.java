package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.MeetingNote;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.repository.MeetingNoteRepository;
import com.example.TalkToDo.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingNoteService {

    @Autowired
    private MeetingNoteRepository meetingNoteRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    public List<MeetingNote> getAllMeetingNotes() {
        return meetingNoteRepository.findAll();
    }

    public Optional<MeetingNote> getMeetingNoteById(Long id) {
        return meetingNoteRepository.findById(id);
    }

    public Optional<List<MeetingNote>> getMeetingNotesByMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .map(meeting -> meetingNoteRepository.findByMeeting(meeting));
    }

    public MeetingNote createMeetingNote(MeetingNote meetingNote) {
        return meetingNoteRepository.save(meetingNote);
    }

    public Optional<MeetingNote> updateMeetingNote(Long id, MeetingNote meetingNoteDetails) {
        return meetingNoteRepository.findById(id)
                .map(existingNote -> {
                    meetingNoteDetails.setId(id);
                    return meetingNoteRepository.save(meetingNoteDetails);
                });
    }

    public boolean deleteMeetingNote(Long id) {
        return meetingNoteRepository.findById(id)
                .map(note -> {
                    meetingNoteRepository.delete(note);
                    return true;
                })
                .orElse(false);
    }
} 