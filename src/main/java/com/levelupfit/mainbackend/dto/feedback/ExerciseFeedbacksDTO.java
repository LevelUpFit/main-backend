package com.levelupfit.mainbackend.dto.feedback;

import lombok.*;

import java.time.LocalDate;
import com.levelupfit.mainbackend.domain.feedbacks.ExerciseFeedbacks;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFeedbacksDTO {

    private int feedbackId;

    private int userId;

    private int exerciseId;

    private String videoUrl;

    private String feedbackText;

    private Float accuracy;

    private LocalDate performedDate;

    private String keypointData;

    public static ExerciseFeedbacksDTO fromEntity(ExerciseFeedbacks feedback) {
        return ExerciseFeedbacksDTO.builder()
                .feedbackId(feedback.getFeedbackId())
                .userId(feedback.getUser().getUserid())
                .exerciseId(feedback.getExercise().getExerciseId())
                .videoUrl(feedback.getVideoUrl())
                .feedbackText(feedback.getFeedbackText())
                .accuracy(feedback.getAccuracy())
                .performedDate(feedback.getPerformedDate())
                .build();
    }

}


