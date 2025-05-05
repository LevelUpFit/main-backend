package com.levelupfit.mainbackend.dto.user.request;

import lombok.Data;

@Data
public class ChangePwdRequestDTO {
    private int userId;
    private String oldPassword;
    private String newPassword;
}
