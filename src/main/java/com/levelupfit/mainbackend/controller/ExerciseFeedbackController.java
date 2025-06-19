package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.feedback.ExerciseFeedbacksDTO;
import com.levelupfit.mainbackend.dto.feedback.request.ExerciseFeedbackRequest;
import com.levelupfit.mainbackend.service.feedback.ExerciseFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-feedback")
public class ExerciseFeedbackController {

    private final ExerciseFeedbackService exerciseFeedbackService;

    @PostMapping("video")
    public ResponseEntity<ApiResponse<ExerciseFeedbacksDTO>> createFeedback(ExerciseFeedbackRequest request) {
        ApiResponse<ExerciseFeedbacksDTO> response = exerciseFeedbackService.createFeedback(request);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/feedback-list")
    public ResponseEntity<ApiResponse<List<ExerciseFeedbacksDTO>>> getFeedbackList(@RequestParam int userId) {
        ApiResponse<List<ExerciseFeedbacksDTO>> response = exerciseFeedbackService.getFeedbackByUserId(userId);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
