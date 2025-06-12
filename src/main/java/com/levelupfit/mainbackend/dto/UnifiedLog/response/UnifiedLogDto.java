package com.levelupfit.mainbackend.dto.UnifiedLog.response;

import com.levelupfit.mainbackend.dto.exerciseLog.ExerciseLogsDTO;
import com.levelupfit.mainbackend.dto.routineLog.RoutineLogsDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UnifiedLogDto {
    private int id;
    private LocalDate performed_date;
    private String log_type;        // "EXERCISE" or "ROUTINE"
    private String name;
    private String target_muscle;   // 루틴일 경우 null 가능
}
