package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4000")
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
}
