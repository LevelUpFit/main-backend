package com.levelupfit.mainbackend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelupfit.mainbackend.dto.feedback.MovementSpeedInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MovementSpeedConverter implements AttributeConverter<MovementSpeedInfo
        , String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(MovementSpeedInfo attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovementSpeedInfo convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        try {
            return objectMapper.readValue(dbData, MovementSpeedInfo.class);
        } catch (Exception e) {
            // 로그 찍고 null 리턴
            return null;
        }
    }

}
