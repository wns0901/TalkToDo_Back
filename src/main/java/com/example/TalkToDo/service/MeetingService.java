package com.example.TalkToDo.service;

import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final UserRepository userRepository;

    private final S3Service s3Service;

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

    public Meeting createMeeting(MultipartFile audioFile) {
        String audioUrl = s3Service.uploadFile(audioFile);
        Meeting meeting = new Meeting();
        meeting.setAudioUrl(audioUrl);
        
        return meeting;
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