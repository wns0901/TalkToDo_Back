package com.example.TalkToDo.dto;

import java.util.List;

import com.example.TalkToDo.entity.TranscriptLine;

import lombok.Data;

@Data
public class TranscriptLineListDTO {
    private List<TranscriptLine> transcriptLineList;
} 