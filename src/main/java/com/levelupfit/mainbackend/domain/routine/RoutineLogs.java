package com.levelupfit.mainbackend.domain.routine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "routine_logs")
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
    private Integer totalVolume;

    @Column(name="duration_seconds")
    private Integer durationSeconds;

    @Column(name="total_sets")
    private Integer totalSets;

    @Column(name="target_muscle")
    private String targetMuscle;

    @Column(name="exercise_details", columnDefinition = "TEXT")
    private String exerciseDetails;

    @Column(name="created_at")
    private LocalDate createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDate.now();
    }

    public static RoutineLogs of(int userId, int routineId, LocalDate performedDate, Integer totalVolume, Integer durationSeconds, Integer totalSets, String targetMuscle, String exerciseDetails) {
        return RoutineLogs.builder()
                .userId(userId)
                .routineId(routineId)
                .performedDate(performedDate)
                .totalVolume(totalVolume)
                .durationSeconds(durationSeconds)
                .totalSets(totalSets)
                .targetMuscle(targetMuscle)
                .exerciseDetails(exerciseDetails)
                .build();
    }
}
