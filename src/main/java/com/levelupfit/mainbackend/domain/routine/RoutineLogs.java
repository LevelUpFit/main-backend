package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineLogs {

    @Id
    @Column(name="routine_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

    @Column(name="routine_id")
    private int routineId;

    @Column(name="performed_date")
    private LocalDate performedDate;

    @Column(name="total_volume")
    private Integer totalVolume;  // 총 볼륨 (kg)

    @Column(name="duration_seconds")
    private Integer durationSeconds;  // 운동 시간 (초)

    @Column(name="total_sets")
    private Integer totalSets;  // 완료한 총 세트 수

    @Column(name="target_muscle")
    private String targetMuscle;  // 타겟 근육

    @Column(name="exercise_details", columnDefinition = "TEXT")
    private String exerciseDetails;  // 운동별 상세 정보 (JSON 문자열로 저장)

    @Column(name="created_at")
    private LocalDate createdAt;

    @PrePersist //save() 할 때 불러와짐
    public void setCreatedAt() {
        this.createdAt = LocalDate.now();
    }
}
