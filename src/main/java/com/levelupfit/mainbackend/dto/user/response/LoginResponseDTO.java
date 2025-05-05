package com.levelupfit.mainbackend.dto.user.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private int code;
    private String message;
    private int userId;
    private String email;
    private String profile;
    private String nickname;
}
