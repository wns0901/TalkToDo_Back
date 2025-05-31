package com.example.TalkToDo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDataDTO {

    private MeetingSummary meetingSummary;
    private List<MeetingTranscript> meetingTranscript;
    private List<Schedule> schedule;
    private List<Todo> todo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MeetingSummary {
        private String subject;
        private String summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MeetingTranscript {
        private double start;
        private double end;
        private String speaker;
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Schedule {
        private LocalDate start;
        private LocalDate end;
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Todo {
        private LocalDateTime start;
        private LocalDateTime end;
        private String text;
    }
}