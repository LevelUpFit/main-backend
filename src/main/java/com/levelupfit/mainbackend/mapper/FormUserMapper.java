package com.levelupfit.mainbackend.mapper;

import com.levelupfit.mainbackend.dto.user.FormUserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FormUserMapper {

    //formUser 저장
    void save(FormUserDTO formuserdto);

    //FormUser찾기
    FormUserDTO findById(String userId);

    void findPassword(String userId, String passwd);
}
