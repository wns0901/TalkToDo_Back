package com.example.TalkToDo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.TalkToDo.dto.MeetingDTO;
import com.example.TalkToDo.dto.MeetingDataDTO;
import com.example.TalkToDo.dto.MeetingNotesDTO;
import com.example.TalkToDo.dto.TodoDTO;
import com.example.TalkToDo.dto.TranscriptLineDTO;
import com.example.TalkToDo.entity.Meeting;
import com.example.TalkToDo.entity.Schedule;
import com.example.TalkToDo.entity.Todo;
import com.example.TalkToDo.entity.TranscriptLine;
import com.example.TalkToDo.entity.User;
import com.example.TalkToDo.repository.MeetingRepository;
import com.example.TalkToDo.repository.ScheduleRepository;
import com.example.TalkToDo.repository.TodoRepository;
import com.example.TalkToDo.repository.TranscriptLineRepository;
import com.example.TalkToDo.repository.UserRepository;
// import com.example.TalkToDo.util.FakeApi;
import com.example.TalkToDo.util.Util;
import com.example.TalkToDo.util.Api;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    // private final FakeApi fakeApi;
    private final Util util;
    private final ScheduleRepository scheduleRepository;
    private final TodoRepository todoRepository;
    private final TranscriptLineRepository transcriptLineRepository;
    private final Api api;

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
        File convertedFile;
        try {
            convertedFile = util.convertToMp3(audioFile);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("오디오 변환 실패", e);
        }
        String audioUrl = s3Service.uploadFile(convertedFile, "audio");
        Long userId = util.getCurrentUserId();
        User user = User.builder().id(userId).build();

        // MeetingDataDTO meetingData = fakeApi.aiApi();
        MeetingDataDTO meetingData = api.getMeetingData(audioFile);

        String wordFileUrl = "";
        try {
            String wordString = util.dataToWordString(meetingData);
            File wordFile = util.stringToWordFile(wordString, meetingData.getMeetingSummary().getSubject() + ".docx");
            wordFileUrl = s3Service.uploadFile(wordFile, "word");
        } catch (IOException e) {
            throw new RuntimeException("워드 파일 생성 실패", e);
        }

        Meeting meeting = Meeting.builder()
                .title(meetingData.getMeetingSummary().getSubject())
                .summary(meetingData.getMeetingSummary().getSummary())
                .createdBy(user)
                .user(user)
                .audioUrl(audioUrl)
                .wordFileUrl(wordFileUrl)
                .build();

        List<Schedule> schedules = meetingData.getSchedule().stream()
                .map(schedule -> Schedule.builder()
                        .title(schedule.getText())
                        .meeting(meeting)
                        .startDate(schedule.getStart().toLocalDate())
                        .endDate(schedule.getEnd().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        List<Todo> todos = meetingData.getTodo().stream()
                .map(todo -> Todo.builder()
                        .title(todo.getText())
                        .meeting(meeting)
                        .startDate(todo.getStart())
                        .dueDate(todo.getEnd())
                        .build())
                .collect(Collectors.toList());

        List<TranscriptLine> transcriptLines = meetingData.getMeetingTranscript().stream()
                .map(transcript -> TranscriptLine.builder()
                        .text(transcript.getText())
                        .meeting(meeting)
                        .startTime(transcript.getStart())
                        .endTime(transcript.getEnd())
                        .speaker(transcript.getSpeaker())
                        .build())
                .collect(Collectors.toList());

                System.out.println("Test:-----------------");
        System.out.println(schedules);
        meetingRepository.save(meeting);
        scheduleRepository.saveAll(schedules);
        todoRepository.saveAll(todos);
        transcriptLineRepository.saveAll(transcriptLines);

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

    @Transactional(readOnly = true)
    public MeetingDTO getMeetingDetails(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        MeetingDTO dto = new MeetingDTO();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setSummary(meeting.getSummary());
        dto.setTasks(meeting.getTasks());

        // 담당자 정보 설정
        if (meeting.getUser() != null) {
            dto.setUserId(meeting.getUser().getId());
            dto.setUserName(meeting.getUser().getName());
        }
        if (meeting.getCreatedBy() != null) {
            dto.setCreatedById(meeting.getCreatedBy().getId());
            dto.setCreatedByName(meeting.getCreatedBy().getName());
        }

        // 할일 목록 변환
        List<TodoDTO> todos = todoRepository.findByMeeting(meeting).stream()
                .map(this::convertToTodoDTO)
                .collect(Collectors.toList());
        dto.setTodos(todos);

        // 회의록 텍스트 변환
        List<TranscriptLineDTO> transcript = transcriptLineRepository.findByMeeting(meeting).stream()
                .map(this::convertToTranscriptLineDTO)
                .collect(Collectors.toList());
        dto.setTranscript(transcript);

        // 회의록 정보 변환
        MeetingNotesDTO notes = new MeetingNotesDTO();
        notes.setTitle(meeting.getTitle());
        notes.setSummary(meeting.getSummary());
        notes.setTasks(meeting.getTasks());
        dto.setNotes(notes);

        return dto;
    }

    @Transactional
    public MeetingNotesDTO updateMeetingNotes(Long meetingId, MeetingNotesDTO notesDTO) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setTitle(notesDTO.getTitle());
        meeting.setSummary(notesDTO.getSummary());
        meeting.setTasks(notesDTO.getTasks());

        Meeting updatedMeeting = meetingRepository.save(meeting);
        return convertToMeetingNotesDTO(updatedMeeting);
    }

    @Transactional
    public List<TranscriptLineDTO> updateTranscript(Long meetingId, List<TranscriptLineDTO> transcriptLines) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        List<TranscriptLine> lines = transcriptLines.stream()
                .map(dto -> {
                    TranscriptLine line = transcriptLineRepository.findById(dto.getId())
                            .orElseThrow(() -> new RuntimeException("Transcript line not found"));
                    line.setText(dto.getText());
                    return transcriptLineRepository.save(line);
                })
                .collect(Collectors.toList());

        return lines.stream()
                .map(this::convertToTranscriptLineDTO)
                .collect(Collectors.toList());
    }

    private TodoDTO convertToTodoDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setType(todo.getType());
        dto.setStartDate(todo.getStartDate());
        dto.setDueDate(todo.getDueDate());
        dto.setAssigneeId(todo.getAssignee() != null ? todo.getAssignee().getId().toString() : null);
        dto.setAssigneeName(todo.getAssignee() != null ? todo.getAssignee().getName() : null);
        dto.setStatus(todo.getStatus());
        dto.setMeetingId(todo.getMeeting() != null ? todo.getMeeting().getId() : null);
        return dto;
    }

    private TranscriptLineDTO convertToTranscriptLineDTO(TranscriptLine line) {
        TranscriptLineDTO dto = new TranscriptLineDTO();
        dto.setId(line.getId());
        dto.setText(line.getText());
        dto.setStart(line.getStartTime());
        dto.setEnd(line.getEndTime());
        dto.setSpeaker(line.getSpeaker());
        return dto;
    }

    private MeetingNotesDTO convertToMeetingNotesDTO(Meeting meeting) {
        MeetingNotesDTO dto = new MeetingNotesDTO();
        dto.setTitle(meeting.getTitle());
        dto.setSummary(meeting.getSummary());
        dto.setTasks(meeting.getTasks());
        return dto;
    }
}