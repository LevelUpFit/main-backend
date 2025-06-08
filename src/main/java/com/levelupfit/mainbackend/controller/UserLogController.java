package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogDateSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.request.LogSearchRequest;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDate;
import com.levelupfit.mainbackend.dto.UnifiedLog.response.UnifiedLogDto;
import com.levelupfit.mainbackend.service.UserLogsServise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins ="http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-logs")
public class UserLogController {

    private final UserLogsServise userLogsServise;

    @GetMapping("/date")
    private ResponseEntity<ApiResponse<List<LocalDate>>> getUnifiedLogDate(@RequestBody LogDateSearchRequest request) {
        ApiResponse<List<LocalDate>> response = userLogsServise.getLogDates(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @GetMapping("/date-detail")
    private ResponseEntity<ApiResponse<List<UnifiedLogDto>>> getUserLogsByUserIdAndDate(@RequestBody LogSearchRequest request) {
        ApiResponse<List<UnifiedLogDto>> response = userLogsServise.getAllUserlogs(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

}
