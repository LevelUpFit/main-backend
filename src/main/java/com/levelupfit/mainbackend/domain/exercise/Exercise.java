package com.levelupfit.mainbackend.domain.exercise;

import com.levelupfit.mainbackend.dto.exercise.request.ExerciseCreateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "exercise")
public class Exercise {

    private static final String DEFAULT_THUMBNAIL = "test";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private int exerciseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String targetMuscle;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private boolean feedbackAvailable;

    public static Exercise of(ExerciseCreateRequest request) {
        return Exercise.builder()
                .name(request.getName())
                .description(request.getDescription())
                .targetMuscle(request.getTargetMuscle())
                .thumbnailUrl(DEFAULT_THUMBNAIL)
                .feedbackAvailable(false)
                .build();
    }
}
