package com.levelupfit.mainbackend.domain.exercise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int exerciseId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "target_muscle")
    private String targetMuscle;

    @Column(name = "feedback_available")
    private boolean feedbackAvailable;

}
