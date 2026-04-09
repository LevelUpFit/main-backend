package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.domain.user.UserStrength;
import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.mapper.FormUserMapper;
import com.levelupfit.mainbackend.mapper.UserMapper;
import com.levelupfit.mainbackend.repository.FormUserRepository;
import com.levelupfit.mainbackend.repository.SocialUserRepository;
import com.levelupfit.mainbackend.repository.UserRepository;
import com.levelupfit.mainbackend.repository.UserStrengthRepository;
import com.levelupfit.mainbackend.util.JwtUtils;
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

    @Value("${DEFAULT_PROFILE_URL}")
    private String DEFAULT_PROFILE_URL;

    @Value("${app.minio.bucket.profile}")
    private String PROFILE_BUCKET;

    @Value("${app.default.profile-image}")
    private String DEFAULT_PROFILE_IMAGE;

    // 이메일 중복 체크
    public void checkEmail(CheckEmailDTO email) {
        if (userRepository.existsByEmail(email.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    // 폼 회원가입
    @Transactional
    public void saveFormUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(registerRequest.getPwd());
        String accessToken = jwtUtils.createAccessToken(registerRequest.getEmail());
        String refreshToken = jwtUtils.createRefreshToken(registerRequest.getEmail());

        User user = User.of(registerRequest, accessToken, refreshToken);
        User savedUser = userRepository.save(user);

        FormUser formUser = FormUser.of(savedUser, encodedPassword);
        formUserRepository.save(formUser);
    }

    // 로그인 로직
    public LoginResponse login(LoginRequestDTO dto) {
        String userEmail = dto.getEmail();
        String password = dto.getPwd();

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        FormUser formUser = formUserRepository.findByUserId(user.getUserid());
        if (formUser == null || !bCryptPasswordEncoder.matches(password, formUser.getPasswd())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return user.toLoginResponse(DEFAULT_PROFILE_URL);
    }

    // 3대 운동 저장
    public ApiResponse<Void> saveUserStrength(UserStrengthDTO dto){
        if(userStrengthRepository.existsByUserId(dto.getUserid())) return ApiResponse.fail(400, "이미 3대 운동 정보가 존재합니다.");
        User user = userRepository.findByUserid(dto.getUserid());
        UserStrength userStrength = UserStrength.builder()
                .user(user)
                .benchPress(dto.getBenchPress())
                .deadLift(dto.getDeadLift())
                .squat(dto.getSquat())
                .build();

        userStrengthRepository.save(userStrength);
        return ApiResponse.ok(201);
    }

    // 리프레시 토큰 찾기
    public UserDTO findByRefreshToken(String refreshToken) {
        return userMapper.findByRefreshToken(refreshToken);
    }

    // 비밀번호 재설정
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

    // 소셜 연동 여부 확인
    public boolean checkLinkForm(String email){
        return socialUserRepository.existsByEmail(email);
    }

    // 유저 정보 조회
    public ApiResponse<LoginResponse> getInfo(int userid) {
        User user = userRepository.findByUserid(userid);
        if(user == null){
            return ApiResponse.fail(404, "유저를 찾을 수 없음");
        }

        return ApiResponse.ok(user.toLoginResponse(DEFAULT_PROFILE_URL));
    }

    // 유저 프로필 수정
    @Transactional
    public ApiResponse<Void> updateProfile(int userId, MultipartFile file) {
        User user = userRepository.findByUserid(userId);
        if(!user.getProfile().equals(DEFAULT_PROFILE_IMAGE)){
            minioService.deleteFile(PROFILE_BUCKET, "", user.getProfile());
        }
        String profile = minioService.uploadFile(PROFILE_BUCKET, "", file);

        if(profile.isEmpty() || profile.isBlank()) {
            user.setProfile(DEFAULT_PROFILE_IMAGE);
            return ApiResponse.fail(500, "프로필 수정 중 오류");
        }
        user.setProfile(profile);
        return ApiResponse.ok();
    }

    // 유저 닉네임 수정
    @Transactional
    public ApiResponse<Void> updateNickname(UpdateNicknameDTO dto) {
        if(dto.getNickname() == null){
            return ApiResponse.fail(400, "닉네임을 입력해주세요");
        }
        User user = userRepository.findByUserid(dto.getUserid());
        user.setNickname(dto.getNickname());

        return ApiResponse.ok();
    }

    // 유저 비밀번호 변경
    @Transactional
    public ApiResponse<Void> updatePassword(ChangePwdRequestDTO dto){
        int userId = dto.getUserId();
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();

        if(userRepository.existsByUserid(userId)){
            User user = userRepository.findByUserid(userId);
            FormUser formUser = formUserRepository.findByUserId(user.getUserid());
            if(bCryptPasswordEncoder.matches(oldPassword, formUser.getPasswd())){
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                formUser.setPasswd(encodedPassword);
                return ApiResponse.ok();
            }
        }
        return ApiResponse.fail(401, "비밀번호 변경 중 오류가 발생했습니다.");
    }

    // 유저 3대 측정 수정
    @Transactional
    public ApiResponse<Void> updateStrength(UserStrengthDTO dto) {
        if(!userStrengthRepository.existsByUserId(dto.getUserid())) {
            return ApiResponse.fail(400, "회원정보를 찾을 수 없습니다.");
        }

        UserStrength userStrength = userStrengthRepository.findByUserId(dto.getUserid());
        userStrength.setBenchPress(dto.getBenchPress());
        userStrength.setDeadLift(dto.getDeadLift());
        userStrength.setSquat(dto.getSquat());

        return ApiResponse.ok();
    }

    // 유저 운동 수준 변경
    @Transactional
    public ApiResponse<Void> updateLevel(UpdateLevelDTO dto) {
        if(dto.getLevel() < 1 || dto.getLevel() > 3){
            return ApiResponse.fail(400, "레벨을 1~3 사이로 입력해주세요.");
        }
        User user = userRepository.findByUserid(dto.getUserid());
        user.setLevel(dto.getLevel());

        return ApiResponse.ok();
    }

    // 계정 탈퇴
    @Transactional
    public ApiResponse<Void> deleteUser(FormUserDTO dto) {
        if(!userRepository.existsByEmail(dto.getUserId())) {
            return ApiResponse.fail(400, "회원정보를 찾을 수 없습니다.");
        }
        User user = userRepository.findByEmail(dto.getUserId());
        if(userStrengthRepository.existsByUserId(user.getUserid())){
            userStrengthRepository.deleteById(user.getUserid());
        }
        FormUser formUser = formUserRepository.findByUserId(user.getUserid());
        String profile = user.getProfile();

        formUserRepository.delete(formUser);
        userRepository.delete(user);

        if(!profile.equals(DEFAULT_PROFILE_IMAGE)){
            minioService.deleteFile(PROFILE_BUCKET, "", profile);
        }

        return ApiResponse.ok();
    }
}
