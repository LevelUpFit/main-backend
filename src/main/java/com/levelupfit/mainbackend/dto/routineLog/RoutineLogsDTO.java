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

    public RoutineLogsDTO(int id, int userId, int routineId, LocalDate performedDate, LocalDate createdDate) {
        this.id = id;
        this.userId = userId;
        this.routineId = routineId;
        this.performedDate = performedDate;
        this.createdDate = createdDate;
    }

    public static RoutineLogsDTO formRoutineLogs(RoutineLogs routineLogs) {
        return new RoutineLogsDTO(
                routineLogs.getId(),
                routineLogs.getUserId(),
                routineLogs.getRoutineId(),
                routineLogs.getPerformedDate(),
                routineLogs.getCreatedAt()
        );
    }

}
