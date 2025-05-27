package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseRequest;
import com.levelupfit.mainbackend.service.RoutineExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routines/{routineId}/exercises")
public class RoutineExerciseController {
    final RoutineExerciseService routineExerciseService;

    //루틴 종목 추가 메서드
    @PostMapping("/exercise")
    public ResponseEntity<ApiResponse<List<RoutineExerciseDTO>>> createRoutineExercise(@RequestBody List<RoutineExerciseRequest> routineExerciseRequest) {
        ApiResponse<List<RoutineExerciseDTO>> apiResponse =routineExerciseService.createRoutineExercise(routineExerciseRequest);
        if(apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else{
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
}
