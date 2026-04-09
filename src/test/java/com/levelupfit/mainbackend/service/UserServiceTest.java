package com.levelupfit.mainbackend.service;

import com.levelupfit.mainbackend.dto.user.request.RegisterRequest;
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
import static org.mockito.ArgumentMatchers.anyString;
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
}
