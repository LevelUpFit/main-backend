package com.levelupfit.mainbackend.dto.feedback.response;

import lombok.Data;

@Data
public class FeedbackresultDTO {
    private String feedback_id;
    private String video_url;
    private String feedback_text;
    private float accuracy;
}
