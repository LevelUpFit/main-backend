package com.levelupfit.mainbackend.dto.routine.request;

import lombok.Data;

@Data
public class RoutinePatchRequest {
    private int routineId;
    private Integer userId;
    private String name;
    private String description;
    private Integer difficulty;
}
