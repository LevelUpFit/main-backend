package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogDateSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDate;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import com.levelupfit.mainbackend.mapper.UnifiedLogMapper;
import com.levelupfit.mainbackend.repository.ExerciseLogsRepository;
import com.levelupfit.mainbackend.repository.RoutineLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLogsServise {

    private final UnifiedLogMapper unifiedLogMapper;

    //날짜 조회 (년 월 입력받음)
    public ApiResponse<List<LocalDate>> getLogDates(LogDateSearchRequest request) {
        try{
            List<LocalDate> list = unifiedLogMapper.findLogDatesByUserAndMonth(request.getUserId(), request.getYear(), request.getMonth());
            return ApiResponse.ok(list,200);
        }catch (Exception e){
            return ApiResponse.fail("날짜 조회중 오류 발생", 500);
        }
    }

    //날짜를 통해 루틴 조회
    public ApiResponse<List<UnifiedLogDto>> getAllUserlogs(LogSearchRequest request) {
        try{

            List<UnifiedLogDto> list = unifiedLogMapper.findAllLogsByUserIdAndDate(request.getUserId(), request.getPerformedDate());

            return ApiResponse.ok(list,200);
        } catch (Exception e){
            return ApiResponse.fail("로그 조회중 오류 발생", 500);
        }

    }

    // 운동 기록 단일 조회 (logId와 logType으로 조회)
    public ApiResponse<UnifiedLogDto> getLogById(int logId, String logType) {
        try {
            // logType 유효성 검사
            if (!"EXERCISE".equals(logType) && !"ROUTINE".equals(logType)) {
                return ApiResponse.fail("잘못된 기록 타입입니다. EXERCISE 또는 ROUTINE만 허용됩니다.", 400);
            }

            UnifiedLogDto log = unifiedLogMapper.findLogById(logId, logType);
            
            if (log == null) {
                return ApiResponse.fail("해당 기록을 찾을 수 없습니다.", 404);
            }
            
            return ApiResponse.ok(log, 200);
        } catch (Exception e) {
            return ApiResponse.fail("기록 조회 중 오류 발생", 500);
        }
    }
}
