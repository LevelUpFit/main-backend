package com.levelupfit.mainbackend.domain.routine;

import com.levelupfit.mainbackend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "routines")
public class Routines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isDefault;
    private Boolean isPublic;
    private int level;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist //save() 할 때 불러와짐
    public void setCreatedAt() {
        this.createdAt = LocalDate.now(); // 현재 시간 설정 yyyy-mm-dd
    }

    private String image;

}
