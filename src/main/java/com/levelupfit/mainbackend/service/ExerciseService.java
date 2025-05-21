package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    public ApiResponse<ExerciseDTO> ExerciseCreate(ExerciseDTO exerciseDTO) {
        //create 구현중
        Exercise exercise = Exercise.builder()
                .name(exerciseDTO.getName())
                .description(exerciseDTO.getDescription())
                .thumbnailUrl(exerciseDTO.getThumbnailUrl())
                .targetMuscle(exerciseDTO.getTargetMuscle())
                .feedbackAvailable(false)
                .build();
        return null;
    }
}
