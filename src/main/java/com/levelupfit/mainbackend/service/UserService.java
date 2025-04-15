package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.FormUserDTO;
import com.levelupfit.mainbackend.dto.SocialUserDTO;
import com.levelupfit.mainbackend.dto.UserDTO;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import com.levelupfit.mainbackend.repository.FormUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final FormUserMapper formUserMapper;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FormUserRepository formUserRepository;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, FormUserMapper formUserMapper, JwtUtils jwtUtils, UserRepository userRepository, FormUserRepository formUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.formUserMapper = formUserMapper;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.formUserRepository = formUserRepository;
    }

    //@Transactional은 데이터베이스 작업을 하나의 작업 단위로 묶어준다. (하나라도 오류 발생하면 오류남)
    //폼회원가입
    @Transactional
    public boolean saveFormUser(FormUserDTO formUserDto, UserDTO userDto, HttpServletResponse response) {
        boolean linked = false;
        try {
            //UserDTO existingUser = userRepository.existsByUserId(userDto.getUser_id());
            boolean existingUser = userRepository.existsByUserid(userDto.getUser_id());
            if (!existingUser) { //신규 회원
                String encodedPassword = bCryptPasswordEncoder.encode(formUserDto.getPwd());
                String accessToken = jwtUtils.createAccessToken(Integer.toString(userDto.getUser_id()));
                String refreshToken = jwtUtils.createRefreshToken(Integer.toString(userDto.getUser_id()));


                User user = User.builder()
                        .email(userDto.getEmail())
                        .dob(userDto.getDob())
                        .level(userDto.getLevel())
                        .gender(userDto.getGender())
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build();

                formUserDto.setPwd(encodedPassword);
                userDto.setAccessToken(accessToken);
                userDto.setRefreshToken(refreshToken);

                User saveduser = userRepository.save(user);

                FormUser formUser = FormUser.builder()
                        .user(saveduser) //여기 로직 손봐야함
                        .passwd(encodedPassword)
                        .build();


                formUserRepository.save(formUser);

                //소셜 로그인 개발 후 생성
//                SocialUserDTO socialuser = socialUserMapper.findByEmail(userDto.getEmail());
//                if (socialuser != null && socialuser.getEmail().equals(userDto.getEmail())) {
//                    socialuser.setUserId(formUserDto.getUserId());
//                    socialUserMapper.save(socialuser);
//                    linked = true;  // 연동 발생 표시
//                }

                //여기부터 조금 다시 확인해야함
//                userMapper.updateAccessTokenAndRefreshToken(userDto.getUser_id(), accessToken, refreshToken);
//
//                Cookie accessTokenCookie = new Cookie("access_token", accessToken);
//                accessTokenCookie.setHttpOnly(true);
//                accessTokenCookie.setSecure(true);
//                accessTokenCookie.setMaxAge(3600);
//                accessTokenCookie.setPath("/");
//                response.addCookie(accessTokenCookie);
            } else {
                throw new RuntimeException("이미 존재하는 사용자입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("회원가입 처리 중 오류가 발생하였습니다.", e);
        }
        return linked;
    }


    //리프레시토큰찾기
    public UserDTO findByRefreshToken(String refreshToken) {
        return userMapper.findByRefreshToken(refreshToken);

    }

    @Transactional
    public void findPassword(String userId, String newPassword) {
        FormUserDTO formUserDto = formUserMapper.findById(userId);
        if (formUserDto == null) {
            throw new RuntimeException("해당 유저가 존재하지 않습니다.");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("비밀번호가 비어있습니다.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        formUserMapper.findPassword(userId, encodedPassword);
    }
}
