package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.domain.user.UserStrength;
import com.levelupfit.mainbackend.dto.*;
import com.levelupfit.mainbackend.service.KakaoService;
import com.levelupfit.mainbackend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> saveFormUser(@RequestBody FormInfoDTO formInfoDto, HttpServletResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "이미 로그인된 사용자입니다."));
        }

        FormUserDTO formUserDto = formInfoDto.getFormUserDto();
        UserDTO userDto = formInfoDto.getUserDto();
        userDto.setProfile("https://test/tset/test.jpg");

        int signed = userService.saveFormUser(formUserDto, userDto, response);

        Map<String, String> responseMap = new HashMap<>();
        return switch (signed) {
            case 201 -> {
                responseMap.put("message", "회원가입이 완료되었습니다.");
                yield ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
            }
            case 409 -> {
                responseMap.put("message", "중복된 이메일 입니다.");
                yield ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
            }
            case 410 -> {
                responseMap.put("message", "이미 카카오로그인으로 등록된 이메일입니다.");
                yield ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
            }
            case 500 -> {
                responseMap.put("message", "회원가입중 오류가 발생했습니다.");
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
            }
            default -> {
                responseMap.put("message", "정의되지 않은 오류가 발생했습니다.");
                yield ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }
        };

    }

    @PostMapping("/strength")
    public ResponseEntity<String> saveStrength(@RequestBody UserStrengthDTO dto, HttpServletResponse response) {
        if(userService.saveUserStrength(dto)){
            return ResponseEntity.status(HttpStatus.OK).body("생성 완료");
        }
        return ResponseEntity.badRequest().body("오류");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO dto, HttpServletResponse response){

        int login = userService.login(dto);

        Map<String, String> responseMap = new HashMap<>();
        return switch (login) {
            case 200 -> {
                responseMap.put("message", "로그인 성공");
                yield ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }
            case 401 -> {
                responseMap.put("message", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                yield ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
            }
            default -> {
                responseMap.put("message", "정의되지 않은 오류가 발생했습니다.");
                yield ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }
        };
    }

    //카카오 로그인 페이지 처리
    @GetMapping("/kakao/login")
    public ResponseEntity<String> loginPage(){
        String kakaoLoginUrl = kakaoService.loginPage();
        return ResponseEntity.ok(kakaoLoginUrl);
    }

    //카카오 로그인 처리가 이루어지는 곳
    @PostMapping("/callback")
    public ResponseEntity<Map<String, String>> checkUser(@RequestParam("code") String code) {
        int result = kakaoService.handleKakaoLogin(code);
        Map<String, String> responseMap = new HashMap<>();
        return switch (result) {
            case 200 -> {
                responseMap.put("message", "로그인 성공");
                yield ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }
            case 401 -> {
                responseMap.put("message", "아이디 혹은 비밀번호가 일치하지 않습니다.");
                yield ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseMap);
            }
            default -> {
                responseMap.put("message", "정의되지 않은 오류가 발생했습니다.");
                yield ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
            }
        };
    }

    //유저 정보 조회
    @GetMapping("/getinfo/{userId}")
    public ResponseEntity<UserResponseDTO> getInfo(@PathVariable int userId){
        UserResponseDTO user = userService.getInfo(userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping("/update/profile")
    public ResponseEntity<Map<String, String>> updateFormUser(@RequestBody FormInfoDTO formInfoDto, HttpServletResponse response) {

        return null;
    }

    @PatchMapping("/level")
    public ResponseEntity<String> updateLevel(@RequestBody UpdateLevelDTO dto) {
        if(userService.updateLevel(dto.getUserid(), dto.getLevel())){
            return ResponseEntity.ok("수정 완료");
        }

        return ResponseEntity.badRequest().body("오류");
    }

    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody UpdateNicknameDTO dto) {
        if(userService.updateNickname(dto.getUserid(), dto.getNickname())){
            return ResponseEntity.ok("수정 완료");
        }

        return ResponseEntity.badRequest().body("오류");
    }
    
    @PatchMapping("/strength")
    public ResponseEntity<String> updateStrength(@RequestBody UserStrengthDTO dto){
        if(userService.updateStrength(dto)){
            return ResponseEntity.ok("수정완료");
        }
        return ResponseEntity.badRequest().body("오류");
    }


}
