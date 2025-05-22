package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.Exercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    //운동 생성
    public ApiResponse<ExerciseDTO> ExerciseCreate(ExerciseCreateRequest exerciseDTO) {
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
    
    //운동 조회
    public ApiResponse<List<ExerciseDTO>> ExerciseFindAll() {
        try{
            List<ExerciseDTO> list = exerciseRepository.findAll()
                    .stream()
                    .map(ExerciseDTO::fromExercise) // Entity -> DTO로 변환
                    .toList(); //List로 변환
            return ApiResponse.ok(list, 200);
        } catch (Exception e){
            return ApiResponse.fail("운동 조회중 오류발생",500);
        }
    }

    //운동 단일 조회
    public ApiResponse<ExerciseDTO> ExerciseFindById(int id) {
        try{
            Exercise exercise = exerciseRepository.findById(id);
            ExerciseDTO dto = ExerciseDTO.fromExercise(exercise);
            return ApiResponse.ok(dto, 200);
        } catch (Exception e){
            return ApiResponse.fail("운동 조회중 오류발생",500);
        }

    }
    
}
