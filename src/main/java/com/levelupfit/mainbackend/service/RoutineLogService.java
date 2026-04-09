package com.levelupfit.mainbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsGetRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.repository.RoutineLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineLogService {

    private final RoutineLogRepository routineLogRepository;
    private final ObjectMapper objectMapper;

    /**
     * 루틴 기록 저장
     */
    @Transactional
    public RoutineLogsDTO saveRoutineLog(RoutineLogsRequest request) {
        try {
            String exerciseDetailsJson = null;
            if (request.getExerciseDetails() != null) {
                exerciseDetailsJson = objectMapper.writeValueAsString(request.getExerciseDetails());
            }

            RoutineLogs logs = RoutineLogs.of(
                    request.getUserId(),
                    request.getRoutineId(),
                    request.getPerformedDate(),
                    request.getTotalVolume(),
                    request.getDurationSeconds(),
                    request.getTotalSets(),
                    request.getTargetMuscle(),
                    exerciseDetailsJson
            );

            RoutineLogs savedLogs = routineLogRepository.save(logs);
            return RoutineLogsDTO.formRoutineLogs(savedLogs);

        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "운동 상세 정보 변환 중 오류 발생");
        }
    }

    /**
     * 루틴 기록 불러오기
     */
    public List<RoutineLogsDTO> getRoutineLogs(RoutineLogsGetRequest request) {
        return routineLogRepository.findByUserId(request.getUserId())
                .stream()
                .map(RoutineLogsDTO::formRoutineLogs)
                .toList();
    }

    /**
     * 기록 삭제
     */
    @Transactional
    public void deleteRoutineLog(RoutineLogsDeleteRequest request) {
        routineLogRepository.deleteById(request.getLogId());
    }
}
