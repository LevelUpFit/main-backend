package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.domain.routine.RoutineExercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseGetRequest;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExercisePatchRequest;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseRequest;
import com.levelupfit.mainbackend.repository.RoutineExerciseRepository;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import com.levelupfit.mainbackend.service.RoutineExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routines-exercises")
public class RoutineExerciseController {
    final RoutineExerciseService routineExerciseService;
    private final RoutineRepository routineRepository;
    private final RoutineExerciseRepository routineExerciseRepository;

    //루틴 종목 추가 메서드
    @PostMapping
    public ResponseEntity<ApiResponse<List<RoutineExerciseDTO>>> createRoutineExercise(@RequestBody List<RoutineExerciseRequest> routineExerciseRequest) {
        ApiResponse<List<RoutineExerciseDTO>> apiResponse =routineExerciseService.createRoutineExercise(routineExerciseRequest);
        if(apiResponse.isSuccess()) {
            return ResponseEntity.ok(apiResponse);
        } else{
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    //루틴 종복 조회 메서드
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineExerciseDTO>>> updateRoutineExercise(@RequestBody RoutineExerciseGetRequest routineExerciseGetRequest) {
        ApiResponse<List<RoutineExerciseDTO>> response = routineExerciseService.getRoutineExercises(routineExerciseGetRequest);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //루틴 종목 수정
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> patchRoutineExercise(@RequestBody RoutineExercisePatchRequest routineExercisePatchRequest) {
        ApiResponse<Void> response = routineExerciseService.patchRoutineExercise(routineExercisePatchRequest);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
