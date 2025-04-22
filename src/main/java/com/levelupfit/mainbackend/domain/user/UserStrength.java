package com.levelupfit.mainbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_strength")
public class UserStrength {
    @Id
    @Column(name="user_id")
    private Integer userId;

    private int squat;

    @Column(name="benchpress")
    private int benchPress;

    @Column(name="deadlift")
    private int deadLift;

    @OneToOne
    @MapsId // user_id를 외래키 + PK로 동시에 사용하는 경우
    @JoinColumn(name = "user_id")
    private User user;  // users 테이블과 1:1 관계
}
