package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.service.KakaoService;
import com.levelupfit.mainbackend.service.MinioService;
import com.levelupfit.mainbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final MinioService minioService;

    // 이메일 중복확인
    @PostMapping("/checkEmail")
    public ResponseEntity<ApiResponse<Void>> checkEmail(@Valid @RequestBody CheckEmailDTO email, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(400, result.getAllErrors().get(0).getDefaultMessage()));
        }
        ApiResponse<Void> response = userService.checkEmail(email);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> saveFormUser(@RequestBody RegisterRequest registerRequest) {
        ApiResponse<String> response = userService.saveFormUser(registerRequest);

        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 3대 운동 등록
    @PostMapping("/strength")
    public ResponseEntity<ApiResponse<Void>> saveStrength(@RequestBody UserStrengthDTO dto) {
        ApiResponse<Void> response = userService.saveUserStrength(dto);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequestDTO dto){
        ApiResponse<LoginResponse> response = userService.login(dto);

        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 카카오 로그인 URL 반환
    @GetMapping("/kakao/login")
    public ResponseEntity<String> loginPage(){
        String kakaoLoginUrl = kakaoService.loginPage();
        return ResponseEntity.ok(kakaoLoginUrl);
    }

    // 카카오 콜백 처리
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> checkUser(@RequestParam("code") String code) {
        ApiResponse<LoginResponse> response = kakaoService.handleKakaoLogin(code);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 유저 정보 조회
    @GetMapping("/getinfo/{userId}")
    public ResponseEntity<ApiResponse<LoginResponse>> getInfo(@PathVariable int userId){
        ApiResponse<LoginResponse> response = userService.getInfo(userId);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 프로필 사진 변경
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateFormUser(@RequestParam MultipartFile profile, @RequestParam int userid) {
        ApiResponse<Void> response = userService.updateProfile(userid, profile);
        if(response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 운동 수준 변경
    @PatchMapping("/level")
    public ResponseEntity<ApiResponse<Void>> updateLevel(@RequestBody UpdateLevelDTO dto) {
        ApiResponse<Void> response = userService.updateLevel(dto);
        if(response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Void>> updateNickname(@RequestBody UpdateNicknameDTO dto) {
        ApiResponse<Void> response = userService.updateNickname(dto);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 3대 운동 변경
    @PatchMapping("/strength")
    public ResponseEntity<ApiResponse<Void>> updateStrength(@RequestBody UserStrengthDTO dto){
        ApiResponse<Void> reponse = userService.updateStrength(dto);
        if(reponse.isSuccess()){
            return ResponseEntity.ok(reponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse);
        }
    }

    // 계정 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody FormUserDTO dto) {
        ApiResponse<Void> response = userService.deleteUser(dto);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody ChangePwdRequestDTO dto) {
        ApiResponse<Void> response = userService.updatePassword(dto);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
