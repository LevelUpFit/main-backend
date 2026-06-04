package com.levelupfit.mainbackend.controller;

import com.levelupfit.mainbackend.dto.ApiResponse;
import com.levelupfit.mainbackend.dto.user.*;
import com.levelupfit.mainbackend.dto.user.request.ChangePwdRequestDTO;
import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
import com.levelupfit.mainbackend.dto.user.response.LoginResponse;
import com.levelupfit.mainbackend.exception.BusinessException;
import com.levelupfit.mainbackend.exception.ErrorCode;
import com.levelupfit.mainbackend.service.KakaoService;
import com.levelupfit.mainbackend.service.UserService;
import com.levelupfit.mainbackend.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtils jwtUtils;

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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequestDTO dto,
                                                            HttpServletResponse httpResponse) {
        LoginResponse loginResponse = userService.login(dto);
        setRefreshTokenCookie(httpResponse, loginResponse.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }

    /**
     * 카카오 로그인 URL 반환
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<String> loginPage() {
        return ResponseEntity.ok(kakaoService.loginPage());
    }

    /**
     * 카카오 콜백 처리
     */
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<LoginResponse>> checkUser(@RequestParam("code") String code,
                                                                HttpServletResponse httpResponse) {
        LoginResponse loginResponse = kakaoService.handleKakaoLogin(code).getData();
        setRefreshTokenCookie(httpResponse, loginResponse.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }

    /**
     * 토큰 재발급 — 쿠키의 refreshToken으로 새 accessToken 발급
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        try {
            String email = jwtUtils.getEmailFromToken(refreshToken);
            String newAccessToken = jwtUtils.createAccessToken(email);
            return ResponseEntity.ok(ApiResponse.ok(Map.of("accessToken", newAccessToken)));
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 로그아웃 — httpOnly 쿠키는 JS에서 삭제 불가하므로 서버에서 만료 처리
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse httpResponse) {
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 유저 정보 조회
     */
    @GetMapping("/getinfo/{userId}")
    public ResponseEntity<ApiResponse<LoginResponse>> getInfo(@PathVariable int userId) {
        LoginResponse response = userService.getInfo(userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /**
     * 프로필 사진 변경
     */
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateFormUser(@RequestParam MultipartFile profile,
                                                            @RequestParam int userid) {
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
    public ResponseEntity<ApiResponse<Void>> updateStrength(@RequestBody UserStrengthDTO dto) {
        userService.updateStrength(dto);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /**
     * 계정 탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody FormUserDTO dto,
                                                        HttpServletResponse httpResponse) {
        userService.deleteUser(dto);
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
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

    // ── private helpers ──────────────────────────────────────────────────────

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)      // TODO: 프로덕션 배포 시 true로 변경
                .sameSite("Strict")
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
