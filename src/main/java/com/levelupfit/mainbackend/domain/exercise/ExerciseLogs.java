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
    int id;

    @Column(name="user_id")
    int userId;

    @Column(name="name")
    String name;

    @Column(name="target_muscle")
    String targetMuscle;

    @Column(name="feedback")
    String feedback;

    @Column(name="performed_date")
    LocalDate performedDate;
}
