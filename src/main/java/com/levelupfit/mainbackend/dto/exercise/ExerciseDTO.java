package com.levelupfit.mainbackend.dto.exercise;

import lombok.Data;

@Data
public class ExerciseDTO {
    private int id;
    private String name;
    private String description;
    private String thumbnailUrl;
    private String targetMuscle;
    private Boolean feedbackAvailable;

}
