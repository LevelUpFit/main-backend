package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsDeleteRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsGetRequest;
import com.levelupfit.mainbackend.dto.exerciseLog.request.ExerciseLogsRequest;
import com.levelupfit.mainbackend.service.ExerciseLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-log")
public class ExerciseLogsController {
    private final ExerciseLogsService exerciseLogsService;

    /**
     * 운동 기록 저장
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Void>> saveExerciseLogs(@RequestBody ExerciseLogsRequest request) {
        exerciseLogsService.saveExerciseLog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201));
    }

    /**
     * 운동 기록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseLogsDTO>>> getAllExerciseLogs(@RequestBody ExerciseLogsGetRequest request) {
        List<ExerciseLogsDTO> result = exerciseLogsService.getExerciseLogs(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * 운동 기록 삭제
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteExerciseLogs(@RequestBody ExerciseLogsDeleteRequest request) {
        exerciseLogsService.deleteExerciseLog(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
