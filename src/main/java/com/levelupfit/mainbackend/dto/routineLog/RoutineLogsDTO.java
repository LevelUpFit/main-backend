package com.levelupfit.mainbackend.dto.routineLog;

import com.levelupfit.mainbackend.domain.routine.RoutineLogs;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutineLogsDTO {
    private int id;
    private int userId;
    private int routineId;
    private LocalDate performedDate;
    private LocalDate createdDate;
    private Integer totalVolume;
    private Integer durationSeconds;
    private Integer totalSets;
    private String targetMuscle;
    private String exerciseDetails;  // JSON 문자열

    public RoutineLogsDTO(int id, int userId, int routineId, LocalDate performedDate, LocalDate createdDate,
                          Integer totalVolume, Integer durationSeconds, Integer totalSets, 
                          String targetMuscle, String exerciseDetails) {
        this.id = id;
        this.userId = userId;
        this.routineId = routineId;
        this.performedDate = performedDate;
        this.createdDate = createdDate;
        this.totalVolume = totalVolume;
        this.durationSeconds = durationSeconds;
        this.totalSets = totalSets;
        this.targetMuscle = targetMuscle;
        this.exerciseDetails = exerciseDetails;
    }

    public static RoutineLogsDTO formRoutineLogs(RoutineLogs routineLogs) {
        return new RoutineLogsDTO(
                routineLogs.getId(),
                routineLogs.getUserId(),
                routineLogs.getRoutineId(),
                routineLogs.getPerformedDate(),
                routineLogs.getCreatedAt(),
                routineLogs.getTotalVolume(),
                routineLogs.getDurationSeconds(),
                routineLogs.getTotalSets(),
                routineLogs.getTargetMuscle(),
                routineLogs.getExerciseDetails()
        );
    }

}
