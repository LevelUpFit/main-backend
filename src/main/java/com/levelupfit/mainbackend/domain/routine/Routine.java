package com.levelupfit.mainbackend.domain.routine;

import com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "routines")
public class Routine {

    private static final String THUMB_BASE_PATH = "levelupfit-profile/exercise/";
    private static final String DEFAULT_THUMB = THUMB_BASE_PATH + "default.png";

    @Id
    @Column(name="routines_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routineId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "target_muscle")
    private String targetMuscle;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDate.now();
    }

    public static Routine of(RoutineCreateRequest request) {
        return Routine.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .targetMuscle(request.getTargetMuscle())
                .thumbnailUrl(generateThumbnailUrl(request.getTargetMuscle()))
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .build();
    }

    private static String generateThumbnailUrl(String targetMuscle) {
        if (targetMuscle == null) return DEFAULT_THUMB;
        
        return switch (targetMuscle) {
            case "하체" -> THUMB_BASE_PATH + "leg.png";
            case "가슴" -> THUMB_BASE_PATH + "chest.png";
            case "어깨" -> THUMB_BASE_PATH + "shoulder.png";
            case "팔" -> THUMB_BASE_PATH + "arm.png";
            case "등" -> THUMB_BASE_PATH + "back.png";
            default -> DEFAULT_THUMB;
        };
    }
}
