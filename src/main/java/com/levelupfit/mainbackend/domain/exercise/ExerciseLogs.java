package com.levelupfit.mainbackend.domain.exercise;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "exercise_logs")
public class ExerciseLogs {

    @Id
    @Column(name="exercise_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

    @Column(name="name")
    private String name;

    @Column(name="target_muscle")
    private String targetMuscle;

    @Column(name="feedback")
    private String feedback;

    @Column(name="performed_date")
    private LocalDate performedDate;

    /**
     * 개별 운동 이름으로 기록 엔티티 생성
     */
    public static ExerciseLogs of(int userId, String name, String targetMuscle, String feedback, LocalDate performedDate) {
        return ExerciseLogs.builder()
                .userId(userId)
                .name(name)
                .targetMuscle(targetMuscle)
                .feedback(feedback)
                .performedDate(performedDate)
                .build();
    }
}
