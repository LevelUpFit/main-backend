package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.domain.user.FormUser;
import com.levelupfit.mainbackend.domain.user.User;
import com.levelupfit.mainbackend.dto.user.LoginRequestDTO;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private FormUserMapper formUserMapper;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FormUserRepository formUserRepository;
    @Mock
    private SocialUserRepository socialUserRepository;
    @Mock
    private UserStrengthRepository userStrengthRepository;
    @Mock
    private MinioService minioService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 시 이미 존재하는 이메일이면 EMAIL_DUPLICATION 예외를 던진다.")
    void saveFormUser_EmailDuplication() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("duplicate@test.com");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.saveFormUser(request);
        });

        assertEquals(ErrorCode.EMAIL_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 LOGIN_FAILED 예외를 던진다.")
    void login_LoginFailed() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("test@test.com");
        dto.setPwd("wrong_pwd");

        User user = User.builder().userid(1).email("test@test.com").build();
        FormUser formUser = FormUser.builder().passwd("encoded_pwd").build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(formUserRepository.findByUserId(anyInt())).thenReturn(formUser);
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login(dto);
        });

        assertEquals(ErrorCode.LOGIN_FAILED, exception.getErrorCode());
    }
}
