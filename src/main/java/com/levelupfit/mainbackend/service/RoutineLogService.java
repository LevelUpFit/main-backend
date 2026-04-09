package com.levelupfit.mainbackend.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsGetRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.repository.RoutineLogRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoutineLogService {

    final RoutineLogRepository routineLogRepository;
    final ObjectMapper objectMapper;

    //루틴 기록 저장
    public ApiResponse<RoutineLogsDTO> saveRoutineLog(RoutineLogsRequest routineLogsRequest) {
        try{
            // exerciseDetails를 JSON 문자열로 변환
            String exerciseDetailsJson = null;
            if (routineLogsRequest.getExerciseDetails() != null) {
                exerciseDetailsJson = objectMapper.writeValueAsString(routineLogsRequest.getExerciseDetails());
            }

            RoutineLogs routineLogs = RoutineLogs.builder()
                    .userId(routineLogsRequest.getUserId())
                    .routineId(routineLogsRequest.getRoutineId())
                    .performedDate(routineLogsRequest.getPerformedDate())
                    .totalVolume(routineLogsRequest.getTotalVolume())
                    .durationSeconds(routineLogsRequest.getDurationSeconds())
                    .totalSets(routineLogsRequest.getTotalSets())
                    .targetMuscle(routineLogsRequest.getTargetMuscle())
                    .exerciseDetails(exerciseDetailsJson)
                    .build();

            RoutineLogs logs = routineLogRepository.save(routineLogs);

            RoutineLogsDTO dto = RoutineLogsDTO.formRoutineLogs(logs);

            return ApiResponse.ok(201, dto);

        } catch (JsonProcessingException e) {
            return ApiResponse.fail(400, "운동 상세 정보 변환 중 오류 발생");
        } catch (Exception e){
            return ApiResponse.fail(500, "루틴 기록중 오류 발생");
        }
    }

    //루틴 기록 불러오기
    public ApiResponse<List<RoutineLogsDTO>> getRoutineLogs(RoutineLogsGetRequest routineLogsRequest) {

        try {
            List<RoutineLogsDTO> list = routineLogRepository.findByUserId(routineLogsRequest.getUserId())
                    .stream()
                    .map(RoutineLogsDTO::formRoutineLogs)
                    .toList();
            return ApiResponse.ok(200, list);
        } catch (Exception  e){
            return ApiResponse.fail(500, "기록 불러오기중 오류 발생");
        }
    }

    //기록 삭제
    public ApiResponse<Void> deleteRoutineLog(RoutineLogsDeleteRequest routineLogsRequest) {
        try{
            routineLogRepository.deleteById(routineLogsRequest.getLogId());
            return ApiResponse.ok(200, null);
        } catch (Exception e){
            return ApiResponse.fail(500, "삭제중 오류");
        }
    }
}
