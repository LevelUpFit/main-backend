package com.levelupfit.mainbackend.mapper;

import com.levelupfit.mainbackend.dto.user.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    
    //유저 저장
    void save (UserDTO userDto);

    //유저 찾기 (user_id)
    UserDTO findByUserId(int user_id);

    //리프레쉬 토큰 업데이트
    void updateAccessTokenAndRefreshToken(int userId, String accessToken, String refreshToken);

    void updateAccessToken(String userId, String accessToken);

    void updateRefreshToken(String userId, String refreshToken);

    void invalidateAccessToken(String accessToken);

    UserDTO findByRefreshToken(String refreshToken);

}
