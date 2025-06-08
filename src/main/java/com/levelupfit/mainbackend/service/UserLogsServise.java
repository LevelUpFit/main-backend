package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
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

    //performed_date : 2025-05-03
    private final ExerciseLogsRepository exerciseLogsRepository;
    private final RoutineLogRepository routineLogsRepository;
    private final UnifiedLogMapper unifiedLogMapper;

    //날짜를 통해 루틴 조회
    public ApiResponse<List<UnifiedLogDto>> getAllUserlogs(LogSearchRequest logSearchRequest) {
        try{

            List<UnifiedLogDto> list = unifiedLogMapper.findAllLogsByUserIdAndDate(logSearchRequest.getUserId(), logSearchRequest.getPerformedDate());

            return ApiResponse.ok(list,200);
        } catch (Exception e){
            return ApiResponse.fail("로그 조회중 오류 발생", 500);
        }

    }
}
