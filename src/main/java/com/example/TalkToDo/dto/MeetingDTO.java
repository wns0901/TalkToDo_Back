package com.example.TalkToDo.dto;

import lombok.Data;
import java.util.List;

@Data
public class MeetingDTO {
    private Long id;
    private String title;
    private String summary;
    private String tasks;
    private List<TodoDTO> todos;
    private List<TranscriptLineDTO> transcript;
    private MeetingNotesDTO notes;
    
    // 담당자 정보 추가
    private Long userId;
    private String userName;
    private Long createdById;
    private String createdByName;
} 