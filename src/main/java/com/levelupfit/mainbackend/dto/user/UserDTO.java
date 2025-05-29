package com.levelupfit.mainbackend.dto.user;

import lombok.Data;

@Data
public class UserDTO {
    private int user_id;
    private String email;
    private String nickname;
    private String dob;
    private int level;
    private String gender;
    private String profile;
    private String accessToken;
    private String refreshToken;
}
