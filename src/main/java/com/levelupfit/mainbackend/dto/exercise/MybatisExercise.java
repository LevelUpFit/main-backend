package com.levelupfit.mainbackend.dto.exercise;

import lombok.Data;

@Data
public class MybatisExercise {
    int exercise_id;
    String name;
    String description;
    String thumbnail_url;
    String target_muscle;
    boolean feedback_available;
}
