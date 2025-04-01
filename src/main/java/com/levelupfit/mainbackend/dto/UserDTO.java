package com.levelupfit.mainbackend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String email;
    private String nickname;
    private String dob;
    private String level;
    private String gender;
    private String profile;
}
