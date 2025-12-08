package com.levelupfit.mainbackend.dto.routineLog.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RoutineLogsRequest {
    private int id;
    private int userId;
    private int routineId;
    private LocalDate performedDate;
    private Integer totalVolume;       // 총 볼륨 (kg)
    private Integer durationSeconds;   // 운동 시간 (초)
    private Integer totalSets;         // 완료한 총 세트 수
    private String targetMuscle;       // 타겟 근육
    private List<ExerciseDetail> exerciseDetails;  // 운동별 상세 정보

    @Data
    public static class ExerciseDetail {
        private int exerciseId;
        private String name;
        private List<SetInfo> sets;
    }

    @Data
    public static class SetInfo {
        private int weight;
        private int reps;
    }
}
