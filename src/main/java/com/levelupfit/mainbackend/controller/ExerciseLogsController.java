package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsGetRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.service.ExerciseLogsService;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseLogsDTO>>> getAllExerciseLogs(@RequestBody ExerciseLogsGetRequest request) {
        ApiResponse<List<ExerciseLogsDTO>> response = exerciseLogsService.getExerciseLogs(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteExerciseLogs(@RequestBody ExerciseLogsDeleteRequest request) {
        ApiResponse<Void> response = exerciseLogsService.deleteExerciseLog(request);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
