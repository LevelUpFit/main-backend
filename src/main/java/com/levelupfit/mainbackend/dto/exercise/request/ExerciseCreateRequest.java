package com.levelupfit.mainbackend.dto.exercise.request;

import lombok.Data;

@Data
public class ExerciseCreateRequest {
    private String name; //운동이름
    private String description; //운동 설명
    private String targetMuscle; //타켓 근육
}
