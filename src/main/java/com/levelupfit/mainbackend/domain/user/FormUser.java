package com.levelupfit.mainbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "form_user")
public class FormUser {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "passwd", nullable = false)
    private String passwd;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public static FormUser of(User user, String encodedPassword) {
        return FormUser.builder()
                .user(user)
                .passwd(encodedPassword)
                .build();
    }
}

