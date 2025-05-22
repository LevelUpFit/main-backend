package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.routine.Routine;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exercise.ExerciseDTO;
import com.levelupfit.mainbackend.dto.routine.RoutineDTO;
import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    //루틴 생성
    public ApiResponse<RoutineDTO> createRoutine(RoutineCreateRequest routineCreateRequest) {
        try{
            Routine routine = Routine.builder()
                    .userId(routineCreateRequest.getUserId())
                    .name(routineCreateRequest.getName())
                    .description(routineCreateRequest.getDescription())
                    .difficulty(routineCreateRequest.getDifficulty())
                    .build();

            Routine createdRoutine = routineRepository.save(routine);

            RoutineDTO dto = RoutineDTO.fromRoutine(createdRoutine);

            return ApiResponse.ok(dto,201);

        } catch (Exception e){
            return ApiResponse.fail("루틴 생성중 오류 발생",500);
        }
    }
}
