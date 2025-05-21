package com.levelupfit.mainbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SocialUserDTO {

    private String userId;
    private String providerType;
    private String email;
}
