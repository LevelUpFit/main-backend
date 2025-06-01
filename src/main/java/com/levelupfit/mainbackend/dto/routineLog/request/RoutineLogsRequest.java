package com.levelupfit.mainbackend.dto.routineLog.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutineLogsRequest {
    private int id;
    private int userId;
    private int routineId;
    private LocalDate performedDate;
}
