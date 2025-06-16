package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.feedback.ExerciseFeedbacksDTO;
import com.levelupfit.mainbackend.dto.feedback.request.ExerciseFeedbackRequest;
import com.levelupfit.mainbackend.service.ExerciseFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
