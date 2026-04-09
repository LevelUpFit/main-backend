package com.levelupfit.mainbackend.domain.routine;

import com.levelupfit.mainbackend.domain.user.User;
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

    @PrePersist //save() 할 때 불러와짐
    public void setCreatedAt() {
        this.createdAt = LocalDate.now(); // 현재 시간 설정 yyyy-mm-dd
    }

    /**
     * 루틴 생성 DTO를 기반으로 Routine 엔티티 생성
     */
    public static Routine of(com.levelupfit.mainbackend.dto.routine.request.RoutineCreateRequest request) {
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
        if (targetMuscle == null) return "levelupfit-profile/exercise/default.png";
        
        return switch (targetMuscle) {
            case "하체" -> "levelupfit-profile/exercise/leg.png";
            case "가슴" -> "levelupfit-profile/exercise/chest.png";
            case "어깨" -> "levelupfit-profile/exercise/shoulder.png";
            case "팔" -> "levelupfit-profile/exercise/arm.png";
            case "등" -> "levelupfit-profile/exercise/back.png";
            default -> "levelupfit-profile/exercise/default.png";
        };
    }
}