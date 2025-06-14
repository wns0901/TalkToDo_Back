package com.example.TalkToDo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TalkToDo.entity.MeetingNote;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.repository.MeetingNoteRepository;
import com.example.TalkToDo.repository.MeetingRepository;

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
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }
        System.out.println("meetingNoteDetails: " + meetingNoteDetails);
        System.out.println("id: " + id);
        return meetingNoteRepository.findById(id)
                .map(existingNote -> {
                    if (meetingNoteDetails.getTitle() != null) {
                        existingNote.setTitle(meetingNoteDetails.getTitle());
                    }
                    if (meetingNoteDetails.getContent() != null) {
                        existingNote.setContent(meetingNoteDetails.getContent());
                    }
                    return meetingNoteRepository.save(existingNote);
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