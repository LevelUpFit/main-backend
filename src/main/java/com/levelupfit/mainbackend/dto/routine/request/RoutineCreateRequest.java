package com.levelupfit.mainbackend.dto.routine.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutineCreateRequest {
    Integer userId;
    String name;
    String description;
    Integer difficulty;
    LocalDate creationDate;
}
