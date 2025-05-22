package com.levelupfit.mainbackend.dto.routine.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutineCreateRequest {

    private Integer userId;
    private String name;
    private String description;
    private Integer difficulty;
    //private LocalDate creationDate; save 할때 자동으로 기입되서 받을 필요 없
}
