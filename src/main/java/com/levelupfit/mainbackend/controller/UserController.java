package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.FormInfoDTO;
import com.levelupfit.mainbackend.dto.FormUserDTO;
import com.levelupfit.mainbackend.dto.LoginRequestDTO;
import com.levelupfit.mainbackend.dto.UserDTO;
import com.levelupfit.mainbackend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

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
        switch (signed){
            case 201:
                //responseMap.put("status", "201");
                responseMap.put("message", "회원가입이 완료되었습니다.");
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(responseMap);
            case 409:
                //responseMap.put("status", "409");
                responseMap.put("message", "중복된 이메일 입니다.");
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(responseMap);
            case 500:
                responseMap.put("status", "500");
                responseMap.put("message", "회원가입중 오류가 발생했습니다.");
        }
//        if (signed) {
//            responseMap.put("message", "회원가입이 완료되었습니다. 기존 소셜 계정과 자동 연동되었습니다.");
//        } else {
//            responseMap.put("message", "회원가입이 성공적으로 완료되었습니다.");
//        }
//        responseMap.put("userId", Integer.toString(userDto.getUser_id()));

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO dto){
        if("test".equals(dto.getEmail()) && "test".equals(dto.getPassword())){
            return ResponseEntity.ok("Success");
        }
        else return ResponseEntity.ok("Fail");
    }
}
