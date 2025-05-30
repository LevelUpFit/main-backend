package com.levelupfit.mainbackend.service;


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

    //루틴 기록 저장
    public ApiResponse<RoutineLogsDTO> saveRoutineLog(RoutineLogsRequest routineLogsRequest) {
        try{
            RoutineLogs routineLogs = RoutineLogs.builder()
                    .userId(routineLogsRequest.getUserId())
                    .routineId(routineLogsRequest.getRoutineId())
                    .performedDate(routineLogsRequest.getPerformedDate())
                    .build();

            RoutineLogs logs = routineLogRepository.save(routineLogs);

            RoutineLogsDTO dto = RoutineLogsDTO.formRoutineLogs(logs);

            return ApiResponse.ok(dto, 201);

        } catch (Exception e){
            return ApiResponse.fail("루틴 기록중 오류 발생", 500);
        }
    }

    //루틴 기록 불러오기
    public ApiResponse<List<RoutineLogsDTO>> getRoutineLogs(RoutineLogsGetRequest routineLogsRequest) {

        try {
            List<RoutineLogsDTO> list = routineLogRepository.findByUserId(routineLogsRequest.getUserId())
                    .stream()
                    .map(RoutineLogsDTO::formRoutineLogs)
                    .toList();
            return ApiResponse.ok(list, 200);
        } catch (Exception  e){
            return ApiResponse.fail("기록 불러오기중 오류 발생", 500);
        }
    }

    //기록 삭제
    public ApiResponse<Void> deleteRoutineLog(RoutineLogsDeleteRequest routineLogsRequest) {
        try{
            routineLogRepository.deleteById(routineLogsRequest.getLogId());
            return ApiResponse.ok(null, 200);
        } catch (Exception e){
            return ApiResponse.fail("삭제중 오류", 500);
        }
    }
}
