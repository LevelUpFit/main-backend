package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.domain.user.UserStrength;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import com.levelupfit.mainbackend.repository.FormUserRepository;
import com.levelupfit.mainbackend.repository.SocialUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.repository.UserStrengthRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.Null;
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
    public ApiResponse<Null> checkEmail(CheckEmailDTO email) {
        if(!userRepository.existsByEmail(email.getEmail())){
            return ApiResponse.ok(null,200);
        } else{
            return ApiResponse.fail("이메일 중복",400);
        }
    }

    //@Transactional은 데이터베이스 작업을 하나의 작업 단위로 묶어준다. (하나라도 오류 발생하면 오류남)
    //폼회원가입
    @Transactional
    public ApiResponse<String> saveFormUser(RegisterRequest registerRequest) {
        boolean linked = false;
        try {
            boolean existingUser = userRepository.existsByEmail(registerRequest.getEmail());
            if (!existingUser) { //신규 회원
                String encodedPassword = bCryptPasswordEncoder.encode(registerRequest.getPwd());
                String accessToken = jwtUtils.createAccessToken(registerRequest.getEmail());
                String refreshToken = jwtUtils.createRefreshToken(registerRequest.getEmail());


                User user = User.builder()
                        .email(registerRequest.getEmail())
                        .nickname("헬린이1")
                        .dob(LocalDate.parse(registerRequest.getDob()))
                        .level(registerRequest.getLevel())
                        .gender(registerRequest.getGender())
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

                LoginResponse userResponse = new LoginResponse();
                userResponse.setUserId(user.getUserid());
                userResponse.setNickname(saveduser.getNickname());
                userResponse.setProfile(DEFAULT_PROFILE_URL);
                userResponse.setAccessToken(accessToken);
                userResponse.setRefreshToken(refreshToken);

                return ApiResponse.ok("",201);

                /*
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
                 */
            }
            else {
                if(checkLinkForm(registerRequest.getEmail())){
                    return ApiResponse.fail("이메일 중복", 400);
                }
                return ApiResponse.fail("회원가입중 오류 발생", 400);
                //throw new RuntimeException("이미 존재하는 사용자입니다.");
            }
        } catch (Exception e) {
            return ApiResponse.fail("회원가입중 오류 발생", 500);
        }
    }

    //로그인 로직
    public ApiResponse<LoginResponse> login(LoginRequestDTO dto){
        String userEmail = dto.getEmail();
        String password = dto.getPwd();

        LoginResponse response = new LoginResponse();

        if(userRepository.existsByEmail(userEmail)){
            //이메일을 통한 user검색
            User user = userRepository.findByEmail(userEmail);
            //user_id를 통한 form_user 검색
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(password,formUser.getPasswd())){
                response.setUserId(user.getUserid());
                response.setNickname(user.getNickname());
                response.setProfile(user.getProfile());
                response.setLevel(user.getLevel());
                response.setAccessToken(user.getAccess_token());
                response.setRefreshToken(user.getRefresh_token());
                return ApiResponse.ok(response,200);
            } else {
                return ApiResponse.fail("아이디 혹은 비밀번호가 일치하지 않습니다.",401);
            }
        } else {
            return ApiResponse.fail("아이디 혹은 비밀번호가 일치하지 않습니다.", 401);
        }
    }

    //3대 운동 저장 (테스트 완)
    public ApiResponse<Null> saveUserStrength(UserStrengthDTO dto){
        if(userStrengthRepository.existsByUserId(dto.getUserid())) return ApiResponse.fail("이미 3대 운동이 존재합니다.",400);
        User user =  userRepository.findByUserid(dto.getUserid());
        UserStrength userStrength = UserStrength.builder()
                .user(user)
                .benchPress(dto.getBenchPress())
                .deadLift(dto.getDeadLift())
                .squat(dto.getSquat())
                .build();

        userStrengthRepository.save(userStrength);

        return ApiResponse.ok(null,201);
    }

    //리프레시토큰찾기
    public UserDTO findByRefreshToken(String refreshToken) {
        return userMapper.findByRefreshToken(refreshToken);

    }

    //비밀번호 찾기
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
    public ApiResponse<LoginResponse> getInfo(int userid) {
        User user = userRepository.findByUserid(userid);
        if(user == null){
            return ApiResponse.fail("유저를 찾을 수 없음", 404);
        }
        LoginResponse userDTO = new LoginResponse();
        userDTO.setUserId(user.getUserid());
        userDTO.setNickname(user.getNickname());
        userDTO.setProfile(DEFAULT_PROFILE_URL + user.getProfile());
        userDTO.setLevel(user.getLevel());
        userDTO.setAccessToken(user.getAccess_token());
        userDTO.setRefreshToken(user.getRefresh_token());

        return ApiResponse.ok(userDTO,200);
    }

    //유저 프로필 수정
    @Transactional
    public ApiResponse<Null> updateProfile(int userId, MultipartFile file) {
        User user = userRepository.findByUserid(userId);
        if(!user.getProfile().equals("default.jpg")){ //기본 프로필인지 확인
            minioService.deleteFile("levelupfit-profile","",user.getProfile()); //기존 프로필 사진 삭제
        }
        String profile = minioService.uploadFile("levelupfit-profile","",file); //프로필 사진 업로드

        if(profile.isEmpty() || profile.isBlank()) {
            user.setProfile("default.jpg");
            return ApiResponse.fail("프로필 수정중 오류", 500);
        }
        user.setProfile(profile);
        return ApiResponse.ok(null,200);
    }

    //유저 닉네임 수정
    @Transactional
    public ApiResponse<Null> updateNickname(UpdateNicknameDTO dto) {
        if(dto.getNickname() == null){
            return ApiResponse.fail("닉네임을 입력해주세요", 400);
        }
        User user = userRepository.findByUserid(dto.getUserid());
        user.setNickname(dto.getNickname());

        return ApiResponse.ok(null,200);
    }

    //유저 비밀번호 변경
    @Transactional
    public ApiResponse<Null> updatePassword(ChangePwdRequestDTO dto){
        int userId = dto.getUserId();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();

        if(userRepository.existsByUserid(userId)){
            User user = userRepository.findByUserid(userId);
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(oldPassword,formUser.getPasswd())){ //비밀번호 확인
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                formUser.setPasswd(encodedPassword);
                return ApiResponse.ok(null,200);
            }
        }
        return ApiResponse.fail("비밀번호 변경 중 오류가 발생했습니다.",401);
    }

    //유저 3대 측정 수정
    @Transactional
    public ApiResponse<Null> updateStrength(UserStrengthDTO dto) {
        if(!userStrengthRepository.existsByUserId(dto.getUserid())) {
            return ApiResponse.fail("회원정보를 찾을 수 없습니다.", 400);
        }

        UserStrength userStrength = userStrengthRepository.findByUserId(dto.getUserid());
        userStrength.setBenchPress(dto.getBenchPress());
        userStrength.setDeadLift(dto.getDeadLift());
        userStrength.setSquat(dto.getSquat());

        return ApiResponse.ok(null,200);
    }

    //유저 운동 수준 변경
    @Transactional
    public ApiResponse<Null> updateLevel(UpdateLevelDTO dto) {
        if(dto.getLevel() < 1 || dto.getLevel() > 3){
            return ApiResponse.fail("레벨을 1~3 사이로 입력해주세요.",400);
        }
        User user = userRepository.findByUserid(dto.getUserid());
        user.setLevel(dto.getLevel());

        return ApiResponse.ok(null,200);
    }

    //계정 탈퇴 (테스트 완)
    @Transactional
    public ApiResponse<Null> deleteUser(FormUserDTO dto) {
        if(!userRepository.existsByEmail(dto.getUserId())) {
            return ApiResponse.fail("회원정보를 찾을 수 없습니다.", 400);
        }
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

        return ApiResponse.ok(null,200);
    }



}
