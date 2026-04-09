package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsGetRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.service.RoutineLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routines-log")
public class RoutineLogsController {

    private final RoutineLogService routineLogService;

    /**
     * 루틴 기록 저장
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<RoutineLogsDTO>> saveRoutineLog(@RequestBody RoutineLogsRequest request) {
        RoutineLogsDTO result = routineLogService.saveRoutineLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201, result));
    }

    /**
     * 루틴 기록 조회 (userId 기반)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineLogsDTO>>> getRoutineLogs(@RequestBody RoutineLogsGetRequest request) {
        List<RoutineLogsDTO> result = routineLogService.getRoutineLogs(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 기록 삭제
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteRoutineLogs(@RequestBody RoutineLogsDeleteRequest request) {
        routineLogService.deleteRoutineLog(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
