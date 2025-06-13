package com.example.TalkToDo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        return attribute != null ? attribute.format(formatter) : null;
    }

    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        return dbData != null ? LocalTime.parse(dbData, formatter) : null;
    }
} 