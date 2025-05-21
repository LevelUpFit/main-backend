package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise")
public class ExercisesController {
    private final ExerciseService exerciseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExerciseDTO>> createExercise(ExerciseDTO dto) {
        ApiResponse<ExerciseDTO> responseDTO = exerciseService.ExerciseCreate(dto);

        //ApiResponse DTO로 반환 하는 로직 구현하기
        return null;
    }
}
