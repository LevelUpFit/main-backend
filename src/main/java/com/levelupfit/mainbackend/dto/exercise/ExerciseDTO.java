package com.levelupfit.mainbackend.dto.exercise;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import lombok.Data;

@Data
public class ExerciseDTO {
    private int id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private String targetMuscle;
    private Boolean feedbackAvailable;

    public ExerciseDTO() {

    }

    public ExerciseDTO(int id, String name, String description, String thumbnailUrl, String targetMuscle, Boolean feedbackAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.targetMuscle = targetMuscle;
        this.feedbackAvailable = feedbackAvailable;
    }

    public static ExerciseDTO fromExercise(Exercise exercise) {
        return new ExerciseDTO(
                exercise.getExerciseId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getThumbnailUrl(),
                exercise.getTargetMuscle(),
                exercise.isFeedbackAvailable()
        );
    }

}
