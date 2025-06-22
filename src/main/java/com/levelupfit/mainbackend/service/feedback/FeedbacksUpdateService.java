package com.levelupfit.mainbackend.service.feedback;

import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;
import com.levelupfit.mainbackend.dto.feedback.response.FeedbackresultDTO;
import com.levelupfit.mainbackend.handler.FeedbackWebSocketHandler;
import com.levelupfit.mainbackend.repository.ExerciseFeedbackRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class FeedbacksUpdateService {

    private final ExerciseFeedbackRepository exerciseFeedbackRepository;
    private final FeedbackWebSocketHandler feedbackWebSocketHandler;

    @Transactional
    public void updateFeedback(FeedbackresultDTO result) {
        ExerciseFeedbacks feedbacks = exerciseFeedbackRepository.findByFeedbackId(result.getFeedback_id());
        feedbacks.setFeedbackText(result.getFeedback_text());
        feedbacks.setVideoUrl(result.getVideo_url());
        feedbacks.setAccuracy(result.getAccuracy());
        feedbacks.setMovementRange(result.getMovementRange());
        feedbacks.setMovementSpeed(result.getMovementSpeed());

        feedbackWebSocketHandler.sendAnalysisCompleteMessage(result.getFeedback_id());
    }
}