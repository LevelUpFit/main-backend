package com.levelupfit.mainbackend.dto.feedback.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFeedbackRequest {

    private int feedbackId;

    private int userId;

    private int exerciseId;

    private int level;

    private MultipartFile video;  // 업로드된 영상의 MinIO URL 또는 로컬 경로

    private boolean isPortrait; //true : 세로, false : 가로

    private LocalDate performedDate;
}
