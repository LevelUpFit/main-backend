package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.domain.user.UserStrength;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.response.LoginResponseDTO;
import com.levelupfit.mainbackend.dto.user.response.MessageResponseDTO;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import com.levelupfit.mainbackend.repository.FormUserRepository;
import com.levelupfit.mainbackend.repository.SocialUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.repository.UserStrengthRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    private final UserStrengthRepository userStrengthRepository;
    private final MinioService minioService;


    @Value("${DEFAULT_PROFILE_URL}") //이건 배포하면서 수정해야함
    private String DEFAULT_PROFILE_URL;

    //이메일 중복 체크
    public boolean checkEmail(String email) {
        System.out.println(email);
        return !userRepository.existsByEmail(email);
    }

    //@Transactional은 데이터베이스 작업을 하나의 작업 단위로 묶어준다. (하나라도 오류 발생하면 오류남)
    //폼회원가입 (테스트 완)
    @Transactional
    public int saveFormUser(FormUserDTO formUserDto, UserDTO userDto, HttpServletResponse response) {
        boolean linked = false;
        try {
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
                        .profile("default.jpg")
                        .access_token(accessToken)
                        .refresh_token(refreshToken)
                        .build();


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
    public LoginResponseDTO login(LoginRequestDTO dto){
        String userEmail = dto.getEmail();
        String password = dto.getPwd();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        if(userRepository.existsByEmail(userEmail)){
            //이메일을 통한 user검색
            User user = userRepository.findByEmail(userEmail);
            //user_id를 통한 form_user 검색
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(password,formUser.getPasswd())){
                loginResponseDTO.setCode(200);
                loginResponseDTO.setMessage("로그인 성공");
                loginResponseDTO.setUserId(user.getUserid());
                loginResponseDTO.setEmail(user.getEmail());
                loginResponseDTO.setNickname(user.getNickname());
                loginResponseDTO.setProfile(user.getProfile());
                return loginResponseDTO;
            } else {
                loginResponseDTO.setCode(401);
                loginResponseDTO.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
                return loginResponseDTO;
            }
        } else {
            loginResponseDTO.setCode(401);
            loginResponseDTO.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
            return loginResponseDTO;
        }
    }

    //3대 운동 저장 (테스트 완)
    public boolean saveUserStrength(UserStrengthDTO dto){
        if(userStrengthRepository.existsByUserId(dto.getUserid())) return false;
        User user =  userRepository.findByUserid(dto.getUserid());
        UserStrength userStrength = UserStrength.builder()
                .user(user)
                .benchPress(dto.getBenchPress())
                .deadLift(dto.getDeadLift())
                .squat(dto.getSquat())
                .build();

        userStrengthRepository.save(userStrength);

        return true;
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

    //유저 정보 조회 (테스트 완)
    public UserResponseDTO getInfo(int userid) {
        System.out.println("email " + userid);
        User user = userRepository.findByUserid(userid);
        if(user == null){
            return null;
        }
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setUser_id(user.getUserid());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setDob(user.getDob().toString());
        userDTO.setGender(user.getGender());
        userDTO.setProfile(DEFAULT_PROFILE_URL + user.getProfile());
        userDTO.setLevel(user.getLevel());
        userDTO.setAccessToken(user.getAccess_token());

        return userDTO;
    }

    //유저 프로필 수정 (테스트 완)
    @Transactional
    public boolean updateProfile(int userId, MultipartFile file) {
        User user = userRepository.findByUserid(userId);
        if(!user.getProfile().equals("default.jpg")){ //기본 프로필인지 확인
            minioService.deleteFile("levelupfit-profile","",user.getProfile()); //기존 프로필 사진 삭제
        }
        String profile = minioService.uploadFile("levelupfit-profile","",file); //프로필 사진 업로드

        if(profile.isEmpty() || profile.isBlank()) {
            user.setProfile("default.jpg");
            return false;
        }
        user.setProfile(profile);
        return true;
    }

    //유저 닉네임 수정 (테스트 완)
    @Transactional
    public boolean updateNickname(int userId, String nickname) {
        if(nickname == null){
            return false;
        }
        User user = userRepository.findByUserid(userId);
        user.setNickname(nickname);

        return true;
    }

    //유저 비밀번호 변경 (개발중)
    @Transactional
    public MessageResponseDTO updatePassword(ChangePwdRequestDTO dto){
        int userId = dto.getUserId();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();

        MessageResponseDTO result = new MessageResponseDTO();
        if(userRepository.existsByUserid(userId)){
            User user = userRepository.findByUserid(userId);
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(oldPassword,formUser.getPasswd())){ //비밀번호 확인
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                formUser.setPasswd(encodedPassword);
                result.setCode(200);
                result.setMessage("비밀번호 변경이 완료되었습니다.");
                return result;
            }
        }

        result.setCode(401);
        result.setMessage("비밀번호 변경 중 오류가 발생했습니다.");
        return result;
    }

    //유저 3대 측정 수정 (테스트 완)
    @Transactional
    public boolean updateStrength(UserStrengthDTO dto) {
        if(!userStrengthRepository.existsByUserId(dto.getUserid())) return false;

        User user = userRepository.findByUserid(dto.getUserid());
        UserStrength userStrength = userStrengthRepository.findByUserId(dto.getUserid());
        userStrength.setBenchPress(dto.getBenchPress());
        userStrength.setDeadLift(dto.getDeadLift());
        userStrength.setSquat(dto.getSquat());

        return true;
    }

    //유저 운동 수준 변경 (테스트 완)
    @Transactional
    public boolean updateLevel(int userId, int level) {
        if(level < 1 || level > 3){
            return false;
        }
        User user = userRepository.findByUserid(userId);
        user.setLevel(level);

        return true;
    }

    //계정 탈퇴 (테스트 완)
    @Transactional
    public boolean deleteUser(FormUserDTO dto) {
        if(!userRepository.existsByEmail(dto.getUserId())) return false;
        User user = userRepository.findByEmail(dto.getUserId());
        if(userStrengthRepository.existsByUserId(user.getUserid())){
            userStrengthRepository.deleteById(user.getUserid());
        }
        FormUser formUser = formUserRepository.findByUserId(user.getUserid());
        String profile = user.getProfile();

        formUserRepository.delete(formUser);
        userRepository.delete(user);

        if(!profile.equals(DEFAULT_PROFILE_URL+"default.jpg")){
            minioService.deleteFile("levelupfit-profile","",profile);
        }

        return true;
    }



}
