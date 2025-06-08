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

    //private String image; 컬럼에 까먹고 추가 안함 나중에 추가 해야함
}