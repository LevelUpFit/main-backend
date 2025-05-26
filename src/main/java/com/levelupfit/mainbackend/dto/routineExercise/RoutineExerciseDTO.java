package com.levelupfit.mainbackend.dto.routineExercise;

import lombok.Data;

import java.util.List;

@Data
public class RoutineExerciseDTO {
    private int routineId;
    private int exerciseId;
    private int sets;
    private List<Integer> reps;
    private Integer restTime;
    private int exerciseOrder;
}
