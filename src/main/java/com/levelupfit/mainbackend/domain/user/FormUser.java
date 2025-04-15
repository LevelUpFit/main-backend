package com.levelupfit.mainbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "form_user")
public class FormUser {

    @Id
    @Column(name = "user_id")
    private Integer userId;  // user_id는 INT니까 Integer 사용

    @Column(name = "passwd", nullable = false)
    private String passwd;

    @OneToOne
    @MapsId // user_id를 외래키 + PK로 동시에 사용하는 경우
    @JoinColumn(name = "user_id")
    private User user;  // users 테이블과 1:1 관계
}

