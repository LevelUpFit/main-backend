package com.levelupfit.mainbackend.domain.exercise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class ExerciseLogs {

    @Id
    @Column(name="exercise_log_id")
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
