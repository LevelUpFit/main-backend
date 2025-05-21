package com.levelupfit.mainbackend.dto.user;

import lombok.Data;

@Data
public class UpdateNicknameDTO {
    private int userid;
    private String nickname;
}
