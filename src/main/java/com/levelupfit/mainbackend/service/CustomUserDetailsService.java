package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.FormUserDTO;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;
    private final FormUserMapper formUserMapper;

    public CustomUserDetailsService(UserMapper userMapper, FormUserMapper formUserMapper) {
        this.userMapper = userMapper;
        this.formUserMapper = formUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        FormUserDTO formUserDto = formUserMapper.findById(username); // userId를 사용
        if (formUserDto == null) {
            throw new UsernameNotFoundException("폼 사용자 정보를 찾을 수 없습니다: " + username);
        }

        // UserDetails 객체 생성
        return new org.springframework.security.core.userdetails.User(
                formUserDto.getUserId(),
                formUserDto.getPwd(), // FormUserDto에서 비밀번호 가져오기
                // 권한 설정 (예: ROLE_USER)
                List.of(() -> "ROLE_USER") // 필요한 경우 권한을 추가
        );
    }
}
