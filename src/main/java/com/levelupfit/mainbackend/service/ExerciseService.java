package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    public ApiResponse<ExerciseCreateRequest> ExerciseCreate(ExerciseCreateRequest exerciseDTO) {
        //create 구현중
        try{
            Exercise exercise = Exercise.builder()
                    .name(exerciseDTO.getName())
                    .description(exerciseDTO.getDescription())
                    .targetMuscle(exerciseDTO.getTargetMuscle())
                    .feedbackAvailable(false) //기본값 false
                    .build();


        } catch (Exception e){
            throw new RuntimeException("DB 저장중 오류", e);
        }

        return null;
    }
}
