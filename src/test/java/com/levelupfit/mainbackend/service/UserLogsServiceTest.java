package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogDateSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.mapper.UnifiedLogMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserLogsServiceTest {

    @Mock
    private UnifiedLogMapper unifiedLogMapper;

    @InjectMocks
    private UserLogsService userLogsService;

    @Test
    @DisplayName("로그 조회 시 잘못된 타입을 넘기면 INVALID_INPUT_VALUE 예외를 던진다.")
    void getLogById_InvalidType() {
        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userLogsService.getLogById(1, "INVALID");
        });

        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그 조회 시 결과가 없으면 INTERNAL_SERVER_ERROR 예외를 던진다.")
    void getLogById_NotFound() {
        // Given
        when(unifiedLogMapper.findLogById(anyInt(), anyString())).thenReturn(null);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userLogsService.getLogById(1, "EXERCISE");
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그 조회 성공 시 데이터를 반환한다.")
    void getLogById_Success() {
        // Given
        UnifiedLogDto mockLog = new UnifiedLogDto();
        mockLog.setId(1);
        when(unifiedLogMapper.findLogById(anyInt(), anyString())).thenReturn(mockLog);

        // When
        UnifiedLogDto result = userLogsService.getLogById(1, "EXERCISE");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}
