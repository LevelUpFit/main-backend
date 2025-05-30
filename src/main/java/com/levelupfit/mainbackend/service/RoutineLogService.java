package com.levelupfit.mainbackend.service;


import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.repository.RoutineLogRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoutineLogService {

    final RoutineLogRepository routineLogRepository;

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
}
