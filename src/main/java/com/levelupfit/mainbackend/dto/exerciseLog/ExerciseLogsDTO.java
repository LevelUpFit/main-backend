package com.levelupfit.mainbackend.dto.exerciseLog;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
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

    public ExerciseLogsDTO(int id, int userId, String name, String targetMuscle, String feedback, LocalDate performedDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetMuscle = targetMuscle;
        this.feedback = feedback;
        this.performedDate = performedDate;
    }

    public static ExerciseLogsDTO fromExerciseLogs(ExerciseLogs exerciseLogs) {
        return new ExerciseLogsDTO(
                exerciseLogs.getId(),
                exerciseLogs.getUserId(),
                exerciseLogs.getName(),
                exerciseLogs.getTargetMuscle(),
                exerciseLogs.getFeedback(),
                exerciseLogs.getPerformedDate()
        );
    }
}
