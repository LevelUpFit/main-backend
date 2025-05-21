package com.levelupfit.mainbackend.dto.user;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String pwd;
}
