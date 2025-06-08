package com.levelupfit.mainbackend.dto.routine;

import com.levelupfit.mainbackend.domain.routine.Routine;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutineDTO {
    private int routineId;
    private Integer userId;
    private String name;
    private String targetMuscle;
    private String thumbnailUrl;
    private String description;
    private int difficulty;
    private LocalDate createdAt;

    public RoutineDTO(int routineId, Integer userId, String name, String targetMuscle, String thumbnailUrl, String description, int difficulty, LocalDate createdAt) {
        this.routineId = routineId;
        this.userId = userId;
        this.name = name;
        this.targetMuscle = targetMuscle;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
    }

    //entitiy -> DTO 메서드
    public static RoutineDTO fromRoutine(Routine routine) {
        return new RoutineDTO(
                routine.getRoutineId(),
                routine.getUserId(),
                routine.getName(),
                routine.getDescription(),
                routine.getTargetMuscle(),
                routine.getThumbnailUrl(),
                routine.getDifficulty(),
                routine.getCreatedAt()
        );
    }
}
