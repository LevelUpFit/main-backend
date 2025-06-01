package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.repository.ExerciseLogsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExerciseLogsService {

    final ExerciseLogsRepository exerciseLogsRepository;

    public ApiResponse<ExerciseLogsDTO> saveExerciseLog(ExerciseLogsRequest request){
        try{

            ExerciseLogs exerciseLogs = ExerciseLogs.builder()
                    .userId(request.getUserId())
                    .name(request.getName())
                    .targetMuscle(request.getTargetMuscle())
                    .feedback(request.getFeedback())
                    .performedDate(request.getPerformedDate())
                    .build();

            ExerciseLogs entity = exerciseLogsRepository.save(exerciseLogs);
            ExerciseLogsDTO dto = ExerciseLogsDTO.fromExerciseLogs(entity);

            return ApiResponse.ok(dto,201);

        } catch(Exception e){
            return ApiResponse.fail("기록 저장중 오류 발생", 500);
        }
    }
}
