package com.levelupfit.mainbackend.dto.user.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String pwd;
    private String gender;
    private String dob;
    private int level;
}
