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
import org.apache.ibatis.jdbc.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final MinioService minioService;

    //이메일 중복확인
    @PostMapping("/checkEmail")
    public ResponseEntity<ApiResponse<Null>> checkEmail(@Valid @RequestBody CheckEmailDTO email, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(result.getAllErrors().get(0).getDefaultMessage(),400));
        }
        ApiResponse<Null> response = userService.checkEmail(email);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //form 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> saveFormUser(@RequestBody RegisterRequest registerRequest) {

        ApiResponse<String> response = userService.saveFormUser(registerRequest);

        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //3대 운동 등록 (테스트 완)
    @PostMapping("/strength")
    public ResponseEntity<ApiResponse<Null>> saveStrength(@RequestBody UserStrengthDTO dto) {
        ApiResponse<Null> response = userService.saveUserStrength(dto);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequestDTO dto){

        ApiResponse<LoginResponse> response = userService.login(dto);

        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //카카오 로그인 페이지 처리
    @GetMapping("/kakao/login")
    public ResponseEntity<String> loginPage(){
        String kakaoLoginUrl = kakaoService.loginPage();
        return ResponseEntity.ok(kakaoLoginUrl);
    }

    //카카오 로그인 처리가 이루어지는 곳
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> checkUser(@RequestParam("code") String code) {
        ApiResponse<LoginResponse> response = kakaoService.handleKakaoLogin(code);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //유저 정보 조회
    @GetMapping("/getinfo/{userId}")
    public ResponseEntity<ApiResponse<LoginResponse>> getInfo(@PathVariable int userId){
        ApiResponse<LoginResponse> response = userService.getInfo(userId);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //프로필 사진 변경
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Null>> updateFormUser(@RequestParam MultipartFile profile, @RequestParam int userid) {
        ApiResponse<Null> response = userService.updateProfile(userid,profile);
        if(response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //운동 수준 변경
    @PatchMapping("/level")
    public ResponseEntity<ApiResponse<Null>> updateLevel(@RequestBody UpdateLevelDTO dto) {
        ApiResponse<Null> response = userService.updateLevel(dto);
        if(response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Null>> updateNickname(@RequestBody UpdateNicknameDTO dto) {
        ApiResponse<Null> response = userService.updateNickname(dto);
        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //3대 운동 변경
    @PatchMapping("/strength")
    public ResponseEntity<ApiResponse<Null>> updateStrength(@RequestBody UserStrengthDTO dto){
        ApiResponse<Null> reponse = userService.updateStrength(dto);
        if(reponse.isSuccess()){
            return ResponseEntity.ok(reponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse);
        }
    }

    //이미지 업로드
    @PostMapping("/upload")
    public void test(MultipartFile file) {
        minioService.uploadFile("levelupfit-profile","",file);
    }

    //계정 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Null>> deleteUser(@RequestBody FormUserDTO dto) {
        ApiResponse<Null> response = userService.deleteUser(dto);
        if(response.isSuccess()){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Null>> updatePassword(@RequestBody ChangePwdRequestDTO dto) {
        ApiResponse<Null> response = userService.updatePassword(dto);
        if(response.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
