package com.levelupfit.mainbackend.dto.feedback.response;

import com.levelupfit.mainbackend.dto.feedback.MovementSpeedInfo;
import lombok.Data;

@Data
public class FeedbackresultDTO {
    private int feedback_id;
    private String video_url;
    private String feedback_text;
    private float accuracy;
    private float movementRange;
    private MovementSpeedInfo movementSpeed;
}
