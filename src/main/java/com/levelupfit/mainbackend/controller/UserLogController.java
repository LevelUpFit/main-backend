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

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-logs")
public class UserLogController {

    private final UserLogsServise userLogsServise;
    
    //@ModelAttribute는 GET/POST용청을 둘다 수용가능하며 param이든 body든 받을 수 있음

    @GetMapping("/date")
    private ResponseEntity<ApiResponse<List<LocalDate>>> getUnifiedLogDate(@ModelAttribute LogDateSearchRequest request) {
        ApiResponse<List<LocalDate>> response = userLogsServise.getLogDates(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

    @GetMapping("/date-detail")
    private ResponseEntity<ApiResponse<List<UnifiedLogDto>>> getUserLogsByUserIdAndDate(@ModelAttribute LogSearchRequest request) {
        ApiResponse<List<UnifiedLogDto>> response = userLogsServise.getAllUserlogs(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(response.getCode()).body(response);
        }
    }

}
