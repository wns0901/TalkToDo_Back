package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public Optional<Meeting> getMeetingById(Long id) {
        return meetingRepository.findById(id);
    }

    public Optional<List<Meeting>> getMeetingsByUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> meetingRepository.findByCreatedBy(user));
    }

    public List<Meeting> getFavoriteMeetings() {
        return meetingRepository.findByFavoriteTrue();
    }

    public Meeting createMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Optional<Meeting> updateMeeting(Long id, Meeting meetingDetails) {
        return meetingRepository.findById(id)
                .map(existingMeeting -> {
                    meetingDetails.setId(id);
                    return meetingRepository.save(meetingDetails);
                });
    }

    public boolean deleteMeeting(Long id) {
        return meetingRepository.findById(id)
                .map(meeting -> {
                    meetingRepository.delete(meeting);
                    return true;
                })
                .orElse(false);
    }
} 