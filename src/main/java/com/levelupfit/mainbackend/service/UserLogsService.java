package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogDateSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.mapper.UnifiedLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLogsService {

    private final UnifiedLogMapper unifiedLogMapper;

    /**
     * 날짜 조회 (년 월 입력받음)
     */
    public List<LocalDate> getLogDates(LogDateSearchRequest request) {
        return unifiedLogMapper.findLogDatesByUserAndMonth(request.getUserId(), request.getYear(), request.getMonth());
    }

    /**
     * 날짜를 통해 통합 로그 조회
     */
    public List<UnifiedLogDto> getAllUserlogs(LogSearchRequest request) {
        return unifiedLogMapper.findAllLogsByUserIdAndDate(request.getUserId(), request.getPerformedDate());
    }

    /**
     * 운동 기록 단일 조회 (logId와 logType으로 조회)
     */
    public UnifiedLogDto getLogById(int logId, String logType) {
        // logType 유효성 검사
        if (!"EXERCISE".equals(logType) && !"ROUTINE".equals(logType)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 기록 타입입니다. EXERCISE 또는 ROUTINE만 허용됩니다.");
        }

        UnifiedLogDto log = unifiedLogMapper.findLogById(logId, logType);
        
        if (log == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "해당 기록을 찾을 수 없습니다.");
        }
        
        return log;
    }
}
