package com.levelupfit.mainbackend.dto.feedback.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFeedbackRequest {

    private int userId;

    private int exerciseId;

    private String videoUrl;  // 업로드된 영상의 MinIO URL 또는 로컬 경로

    private LocalDate performedDate;
}
