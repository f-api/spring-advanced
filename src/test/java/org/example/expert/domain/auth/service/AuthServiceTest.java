package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_이미_존재하는_이메일이면_예외발생() {
        // Given (테스트 데이터 준비)
        SignupRequest request = new SignupRequest("test@example.com", "password123", "USER");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true); // 이메일이 이미 존재한다고 가정

        // When & Then (예외가 발생하는지 검증)
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> authService.signup(request));

        // 예외 메시지 검증
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }
}

