package com.levelupfit.mainbackend.domain.exercise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exercises")
public class Exercise {

    @Id
    @Column(name = "exercise_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //이게 Serial 처리
    private int exerciseId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "target_muscle", columnDefinition = "TEXT")
    private String targetMuscle;

    @Column(name = "feedback_available")
    private boolean feedbackAvailable;

}
