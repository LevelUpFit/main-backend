package com.levelupfit.mainbackend.dto.feedback;

import lombok.Data;

@Data
public class MovementSpeedInfo {
    private float avgContractionTime;
    private float avgRelaxationTime;
    private int contractionPercent;
    private int relaxationPercent;
}
