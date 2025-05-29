package com.levelupfit.mainbackend.dto.user.response;

import lombok.Data;

@Data
public class LoginResponse {
    private int UserId;
    private String nickname;
    private String profile;
    private int level;
    private String accessToken;
    private String refreshToken;
}
