package com.levelupfit.mainbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.repository.RoutineLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutineLogServiceTest {

    @Mock
    private RoutineLogRepository routineLogRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RoutineLogService routineLogService;

    @Test
    @DisplayName("루틴 기록 저장 시 성공적으로 레포지토리를 호출한다.")
    void saveRoutineLog_Success() throws Exception {
        // Given
        RoutineLogsRequest request = new RoutineLogsRequest();
        request.setUserId(1);
        request.setRoutineId(1);
        request.setPerformedDate(LocalDate.now());
        
        when(routineLogRepository.save(any(RoutineLogs.class))).thenReturn(RoutineLogs.builder().build());

        // When
        routineLogService.saveRoutineLog(request);

        // Then
        verify(routineLogRepository, times(1)).save(any(RoutineLogs.class));
    }
}
