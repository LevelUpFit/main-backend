package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsGetRequest;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.service.RoutineLogService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routines-log")
public class RoutineLogsController {

    final RoutineLogService routineLogService;

    //기록 저장
    @PostMapping("save")
    public ResponseEntity<ApiResponse<RoutineLogsDTO>> saveRoutineLog(@RequestBody RoutineLogsRequest request) {
        ApiResponse<RoutineLogsDTO> response = routineLogService.saveRoutineLog(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //기록 조회(userId)
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineLogsDTO>>> getRoutineLogs(@RequestBody RoutineLogsGetRequest request) {
        ApiResponse<List<RoutineLogsDTO>> response = routineLogService.getRoutineLogs(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    //기록 삭제
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteRoutineLogs(@RequestBody RoutineLogsDeleteRequest request) {
        ApiResponse<Void> response = routineLogService.deleteRoutineLog(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.badRequest().body(response);
        }
    }
}
