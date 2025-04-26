package com.levelupfit.mainbackend.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
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

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private LocalDate dob;

    @Column(name = "user_level", nullable = true)
    private int level;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String profile;

    @Column(nullable = false)
    private String access_token;

    @Column(nullable = false)
    private String refresh_token;
}
