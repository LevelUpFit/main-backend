package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.MybatisExercise;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
public class ExercisesController {
    private final ExerciseService exerciseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(@RequestBody ExerciseCreateRequest dto) {
        ApiResponse<ExerciseDTO> responseDTO = exerciseService.ExerciseCreate(dto);
        if(responseDTO.isSuccess()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseDTO>>> getExercise() {
        ApiResponse<List<ExerciseDTO>> responseDTO = exerciseService.ExerciseFindAll();
        if(responseDTO.isSuccess()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseDTO>> getExerciseById(@PathVariable int id) {
        ApiResponse<ExerciseDTO> responseDTO = exerciseService.ExerciseFindById(id);
        if(responseDTO.isSuccess()) {
            return ResponseEntity.ok(responseDTO);
        } else{
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/feedback-exercise")
    public ResponseEntity<ApiResponse<List<MybatisExercise>>> getFeedbackExercises() {
        ApiResponse<List<MybatisExercise>> responseDTO = exerciseService.findFeedbackExercises();
        if(responseDTO.isSuccess()) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
