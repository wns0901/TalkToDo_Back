package com.example.TalkToDo.dto;

import lombok.Data;

@Data
public class TranscriptLineDTO {
    private Long id;
    private String text;
    private double start;
    private double end;
    private String speaker;
} 