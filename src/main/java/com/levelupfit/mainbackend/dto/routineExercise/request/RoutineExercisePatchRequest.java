package com.levelupfit.mainbackend.dto.routineExercise.request;

import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoutineExercisePatchRequest {
    private List<RoutineExerciseDTO> existingRoutineExerciseList; //기존 운동 종목
    private List<RoutineExerciseRequest> newRoutineExerciseList;  //추가 운동 종목
}
