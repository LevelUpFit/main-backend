package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.routine.RoutineExercise;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineExercise.RoutineExerciseDTO;
import com.levelupfit.mainbackend.dto.routineExercise.request.RoutineExerciseRequest;
import com.levelupfit.mainbackend.repository.ExerciseRepository;
import com.levelupfit.mainbackend.repository.RoutineExerciseRepository;
import com.levelupfit.mainbackend.repository.RoutineRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {

    final RoutineExerciseRepository routineExerciseRepository;
    final RoutineRepository routineRepository;
    final ExerciseRepository exerciseRepository;

    public ApiResponse<List<RoutineExerciseDTO>> createRoutineExercise(List<RoutineExerciseRequest> routineExerciseDTOList) {
        try{
            int id = routineExerciseDTOList.get(0).getRoutineId();
            for(RoutineExerciseRequest routineExerciseDTO : routineExerciseDTOList) {
                if(routineExerciseDTO == null) {
                    return ApiResponse.fail("운동 정보가 없습니다.",400);
                } else if(routineExerciseDTO.getSets() != routineExerciseDTO.getReps().size()){
                    return ApiResponse.fail("sets 수보다 더 많은 reps를 지정할 수 없습니다.",400);
                }

                RoutineExercise routineExercise = RoutineExercise.builder()
                        .routine(routineRepository.findByRoutineId(routineExerciseDTO.getRoutineId()))
                        .exercise(exerciseRepository.findById(routineExerciseDTO.getExerciseId()))
                        .sets(routineExerciseDTO.getSets())
                        .reps(routineExerciseDTO.getReps().toArray(new Integer[0]))
                        .restTime(routineExerciseDTO.getRestTime())
                        .exerciseOrder(routineExerciseDTO.getRoutineId())
                        .build();

                routineExerciseRepository.save(routineExercise);
            }
            List<RoutineExerciseDTO> routineExerciseList = routineExerciseRepository.findByRoutineRoutineId(id)
                    .stream()
                    .map(RoutineExerciseDTO::fromRoutineExercise)
                    .toList();


            return ApiResponse.ok(routineExerciseList,200);
        } catch (Exception e){
            return ApiResponse.fail("루틴 운동 저장중 오류가 발생했습니다.",400);
        }
    }
}
