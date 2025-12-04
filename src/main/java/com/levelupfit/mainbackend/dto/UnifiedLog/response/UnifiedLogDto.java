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
    private String target_muscle;
    
    // 운동 기록 전용 필드
    private String feedback;        // 느낀점
    
    // 루틴 기록 전용 필드
    private Integer routine_id;         // 루틴 ID
    private Integer total_volume;       // 총 볼륨 (kg)
    private Integer duration_seconds;   // 운동 시간 (초)
    private Integer total_sets;         // 완료한 총 세트 수
    private String exercise_details;    // 운동별 상세 정보 (JSON)
}
