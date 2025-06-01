package com.levelupfit.mainbackend.dto.exerciseLog;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExerciseLogsDTO {

    private int id;
    private int userId;
    private String name;
    private String targetMuscle;
    private String feedback;
    private LocalDate performedDate;
}
