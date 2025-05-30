package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.request.RoutineLogsRequest;
import com.levelupfit.mainbackend.service.RoutineLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/routines-log")
public class RoutineLogsController {

    final RoutineLogService routineLogService;

    @PostMapping("save")
    public ResponseEntity<ApiResponse<RoutineLogsDTO>> saveRoutineLog(@RequestBody RoutineLogsRequest request) {
        ApiResponse<RoutineLogsDTO> response = routineLogService.saveRoutineLog(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
