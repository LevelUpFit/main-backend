package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.exercise.ExerciseLogs;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsGetRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.repository.ExerciseLogsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseLogsService {

    final ExerciseLogsRepository exerciseLogsRepository;

    //운동 기록 저장
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
    
    //운동 기록 조회
    public ApiResponse<List<ExerciseLogsDTO>> getExerciseLogs(ExerciseLogsGetRequest request){
        try{
            List<ExerciseLogsDTO> list = exerciseLogsRepository.findAllByUserId(request.getUserId())
                    .stream()
                    .map(ExerciseLogsDTO::fromExerciseLogs)
                    .toList();
            return ApiResponse.ok(list,200);
            
        } catch (Exception e){
            return ApiResponse.fail("기록 조회중 오류", 500);
        }
    }
    
    //운동 기록 삭제
    public ApiResponse<Void> deleteExerciseLog(ExerciseLogsDeleteRequest request){
        try{
            exerciseLogsRepository.deleteById(request.getExerciseLogId());
            return ApiResponse.ok(null,200);
        } catch(Exception e){
            return ApiResponse.fail("삭제중 오류 발생", 500);
        }
    }
}
