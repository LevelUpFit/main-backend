package com.levelupfit.mainbackend.dto.user.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoginResponse {
    private int UserId;
    private String nickname;
    private String profile;
    private int level;
    private String accessToken;

    @JsonIgnore  // JSON body에 포함하지 않음 — 컨트롤러에서 httpOnly 쿠키로 설정
    private String refreshToken;
}
