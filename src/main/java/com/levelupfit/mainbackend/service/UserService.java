package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.*;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import com.levelupfit.mainbackend.repository.FormUserRepository;
import com.levelupfit.mainbackend.repository.SocialUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final FormUserMapper formUserMapper;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final FormUserRepository formUserRepository;
    private final SocialUserRepository socialUserRepository;

    //@Transactional은 데이터베이스 작업을 하나의 작업 단위로 묶어준다. (하나라도 오류 발생하면 오류남)
    //폼회원가입
    @Transactional
    public int saveFormUser(FormUserDTO formUserDto, UserDTO userDto, HttpServletResponse response) {
        boolean linked = false;
        try {
            //UserDTO existingUser = userRepository.existsByUserId(userDto.getUser_id());
            boolean existingUser = userRepository.existsByEmail(userDto.getEmail());
            if (!existingUser) { //신규 회원
                String encodedPassword = bCryptPasswordEncoder.encode(formUserDto.getPwd());
                String accessToken = jwtUtils.createAccessToken(Integer.toString(userDto.getUser_id()));
                String refreshToken = jwtUtils.createRefreshToken(Integer.toString(userDto.getUser_id()));


                User user = User.builder()
                        .email(userDto.getEmail())
                        .nickname("헬린이1")
                        .dob(LocalDate.parse(userDto.getDob()))
                        .level(userDto.getLevel())
                        .gender(userDto.getGender())
                        .profile("test")
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build();


                //너네 왜 있니...?
//                formUserDto.setPwd(encodedPassword);
//                userDto.setAccessToken(accessToken);
//                userDto.setRefreshToken(refreshToken);

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
            }
            else {
                if(checkLinkForm(userDto.getEmail())){
                    return 410;
                }
                return 409;
                //throw new RuntimeException("이미 존재하는 사용자입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 저장 중 오류", e);
            //return 500;
            //throw new RuntimeException("회원가입 처리 중 오류가 발생하였습니다.", e);
        }
        return 201;
    }

    //로그인 로직
    public int login(LoginRequestDTO dto){
        String userId = dto.getEmail();
        String password = dto.getPassword();

        if(userRepository.existsByEmail(userId)){
            //이메일을 통한 user검색
            User user = userRepository.findByEmail(userId);
            //user_id를 통한 form_user 검색
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(password,formUser.getPasswd())){
                return 200;
            } else {
                return 401;
            }
        } else {
            return 401;
        }
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

    //같은 email의 social회원이 있으면 참 없으면 거짓
    public boolean checkLinkForm(String email){
        return socialUserRepository.existsByEmail(email);
    }

    //유저 정보 조회
    public UserResponseDTO getInfo(int userid) {
        System.out.println("email " + userid);
        User user = userRepository.findByUserid(userid);
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setUser_id(user.getUserid());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setDob(user.getDob().toString());
        userDTO.setGender(user.getGender());
        userDTO.setProfile(user.getProfile());
        userDTO.setLevel(user.getLevel());
        userDTO.setAccessToken(user.getAccess_token());

        return userDTO;
    }

    //유저 정보 수정
    @Transactional
    public void updateUserInfo(int userId, UserDTO userDto) {
        User user = userRepository.findByUserid(userId);
        user.set
    }
}
