package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogDateSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import com.levelupfit.mainbackend.service.UserLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-logs")
public class UserLogController {

    private final UserLogsService userLogsService;

    /**
     * 날짜 조회 (년 월 입력받음)
     */
    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<LocalDate>>> getUnifiedLogDate(@ModelAttribute LogDateSearchRequest request) {
        List<LocalDate> result = userLogsService.getLogDates(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 날짜를 통해 통합 로그 조회
     */
    @GetMapping("/date-detail")
    public ResponseEntity<ApiResponse<List<UnifiedLogDto>>> getUserLogsByUserIdAndDate(@ModelAttribute LogSearchRequest request) {
        List<UnifiedLogDto> result = userLogsService.getAllUserlogs(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 운동 기록 단일 조회 (logId와 logType으로 조회)
     */
    @GetMapping("/detail/{logId}")
    public ResponseEntity<ApiResponse<UnifiedLogDto>> getLogById(
            @PathVariable int logId,
            @RequestParam String logType) {
        UnifiedLogDto result = userLogsService.getLogById(logId, logType);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

}
