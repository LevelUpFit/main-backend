package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.service.ExerciseLogsService;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-log")
public class ExerciseLogsController {
    private final ExerciseLogsService exerciseLogsService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ExerciseLogsDTO>> saveExerciseLogs(@RequestBody ExerciseLogsRequest request) {
        ApiResponse<ExerciseLogsDTO> response = exerciseLogsService.saveExerciseLog(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
