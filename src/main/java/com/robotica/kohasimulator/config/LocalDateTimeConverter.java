package com.robotica.kohasimulator.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        // SQLite stores as "2026-01-15 10:30:00", ISO requires "T" separator
        return LocalDateTime.parse(dbData.replace(" ", "T"));
    }
}
