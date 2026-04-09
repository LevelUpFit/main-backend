package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.service.KakaoService;
import com.levelupfit.mainbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    /**
     * 이메일 중복확인
     */
    @PostMapping("/checkEmail")
    public ResponseEntity<ApiResponse<Void>> checkEmail(@Valid @RequestBody CheckEmailDTO email) {
        userService.checkEmail(email);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> saveFormUser(@RequestBody RegisterRequest registerRequest) {
        userService.saveFormUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201));
    }

    /**
     * 3대 운동 등록
     */
    @PostMapping("/strength")
    public ResponseEntity<ApiResponse<Void>> saveStrength(@RequestBody UserStrengthDTO dto) {
        userService.saveUserStrength(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(201));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequestDTO dto){
        LoginResponse response = userService.login(dto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * 카카오 로그인 URL 반환
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<String> loginPage(){
        return ResponseEntity.ok(kakaoService.loginPage());
    }

    /**
     * 카카오 콜백 처리
     */
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> checkUser(@RequestParam("code") String code) {
        LoginResponse response = kakaoService.handleKakaoLogin(code).getData(); // KakaoService도 리팩토링 필요할 수 있음
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * 유저 정보 조회
     */
    @GetMapping("/getinfo/{userId}")
    public ResponseEntity<ApiResponse<LoginResponse>> getInfo(@PathVariable int userId){
        LoginResponse response = userService.getInfo(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * 프로필 사진 변경
     */
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateFormUser(@RequestParam MultipartFile profile, @RequestParam int userid) {
        userService.updateProfile(userid, profile);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 운동 수준 변경
     */
    @PatchMapping("/level")
    public ResponseEntity<ApiResponse<Void>> updateLevel(@RequestBody UpdateLevelDTO dto) {
        userService.updateLevel(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 닉네임 변경
     */
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(@RequestBody UpdateNicknameDTO dto) {
        userService.updateNickname(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 3대 운동 변경
     */
    @PatchMapping("/strength")
    public ResponseEntity<ApiResponse<Void>> updateStrength(@RequestBody UserStrengthDTO dto){
        userService.updateStrength(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 계정 탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody FormUserDTO dto) {
        userService.deleteUser(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody ChangePwdRequestDTO dto) {
        userService.updatePassword(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
