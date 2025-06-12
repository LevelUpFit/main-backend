package com.levelupfit.mainbackend.dto.routineExercise.request;

import lombok.Data;

import java.util.List;

@Data
public class RoutineExerciseRequest {
    private int routineId;
    private int exerciseId;
    private int sets;
    private List<Integer> reps;
    private List<Integer> weight;
    private Integer restTime;
    private int exerciseOrder;
}
