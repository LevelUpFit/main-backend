package com.levelupfit.mainbackend.domain.user;

import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
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

    private static final String DEFAULT_NICKNAME = "헬린이1";
    private static final String DEFAULT_PROFILE = "default.jpg";

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

    public static User of(RegisterRequest request, String accessToken, String refreshToken) {
        return User.builder()
                .email(request.getEmail())
                .nickname(DEFAULT_NICKNAME)
                .dob(LocalDate.parse(request.getDob()))
                .level(request.getLevel())
                .gender(request.getGender())
                .profile(DEFAULT_PROFILE)
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
    }

    /**
     * User 엔티티를 LoginResponse DTO로 변환
     */
    public com.levelupfit.mainbackend.dto.user.response.LoginResponse toLoginResponse(String baseUrl) {
        com.levelupfit.mainbackend.dto.user.response.LoginResponse response = new com.levelupfit.mainbackend.dto.user.response.LoginResponse();
        response.setUserId(this.userid);
        response.setNickname(this.nickname);
        response.setProfile(baseUrl + this.profile);
        response.setLevel(this.level);
        response.setAccessToken(this.access_token);
        response.setRefreshToken(this.refresh_token);
        return response;
    }
}
