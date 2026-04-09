package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.MybatisExercise;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
public class ExercisesController {
    private final ExerciseService exerciseService;

    /**
     * 운동 생성
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(@RequestBody ExerciseCreateRequest dto) {
        ExerciseDTO result = exerciseService.createExercise(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201, result));
    }

    /**
     * 모든 운동 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseDTO>>> getExercise() {
        List<ExerciseDTO> result = exerciseService.findAllExercises();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 운동 단일 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> getExerciseById(@PathVariable int id) {
        ExerciseDTO result = exerciseService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 피드백 가능 운동 조회
     */
    @GetMapping("/feedback-exercise")
    public ResponseEntity<ApiResponse<List<MybatisExercise>>> getFeedbackExercises() {
        List<MybatisExercise> result = exerciseService.findFeedbackExercises();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
