package com.levelupfit.mainbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userid;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    @Column(nullable = false)
    private String dob;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String gender;

    private String profile;

    @Column(nullable = false)
    private String access_token;

    @Column(nullable = false)
    private String refresh_token;
}
