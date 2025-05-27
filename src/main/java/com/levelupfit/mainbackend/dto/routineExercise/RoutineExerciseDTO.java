package com.levelupfit.mainbackend.dto.routineExercise;

import com.levelupfit.mainbackend.domain.routine.RoutineExercise;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class RoutineExerciseDTO {
    private int id;
    private int routineId;
    private int exerciseId;
    private int sets;
    private List<Integer> reps;
    private Integer restTime;
    private int exerciseOrder;

    public RoutineExerciseDTO(int id, int routineId, int exerciseId, int sets, List<Integer> reps, Integer restTime, int exerciseOrder) {
        this.id = id;
        this.routineId = routineId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.restTime = restTime;
        this.exerciseOrder = exerciseOrder;
    }

    public static RoutineExerciseDTO fromRoutineExercise(RoutineExercise routineExercise) {
        return new RoutineExerciseDTO(
                routineExercise.getId(),
                routineExercise.getRoutine().getRoutineId(),
                routineExercise.getExercise().getExerciseId(),
                routineExercise.getSets(),
                Arrays.asList(routineExercise.getReps()), //배열 -> List
                routineExercise.getRestTime(),
                routineExercise.getExerciseOrder()
        );
    }
}
