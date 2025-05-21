package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ApiResponse<ExerciseDTO> ExerciseCreate(ExerciseCreateRequest exerciseDTO) {
        //create 구현중
        try{
            Exercise exercise = Exercise.builder()
                    .name(exerciseDTO.getName())
                    .description(exerciseDTO.getDescription())
                    .targetMuscle(exerciseDTO.getTargetMuscle())
                    .thumbnailUrl("test")
                    .feedbackAvailable(false) //기본값 false
                    .build();

            Exercise createdExercise = exerciseRepository.save(exercise); //JPA 저장
            
            //DTO로 변환
            ExerciseDTO createdExerciseDTO = new ExerciseDTO();
            createdExerciseDTO.setId(createdExercise.getExerciseId());
            createdExerciseDTO.setName(createdExercise.getName());
            createdExerciseDTO.setDescription(createdExercise.getDescription());
            createdExerciseDTO.setTargetMuscle(createdExercise.getTargetMuscle());
            createdExerciseDTO.setThumbnailUrl(createdExercise.getThumbnailUrl());
            createdExerciseDTO.setFeedbackAvailable(false);

            //DTO 대입해서 API반환 DTO에 넣기
            return ApiResponse.ok(createdExerciseDTO, 201);


        } catch (Exception e){
            return ApiResponse.fail("운동 생성중 오류발생",500);

        }
    }
}
