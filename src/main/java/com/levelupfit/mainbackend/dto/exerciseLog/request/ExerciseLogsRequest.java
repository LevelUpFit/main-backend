package com.levelupfit.mainbackend.dto.exerciseLog.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExerciseLogsRequest {
    private int userId;
    private String name;
    private String targetMuscle;
    private String feedback;
    private LocalDate performedDate;
}
