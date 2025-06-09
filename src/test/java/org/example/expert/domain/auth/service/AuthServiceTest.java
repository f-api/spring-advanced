package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Mockito를 사용하여 Mock 객체를 주입
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // 가짜 객체 생성
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    // @Mock으로 생성된 Mock 객체들을 AuthService 인스턴스에 주입
    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;

    // @BeforeEach 어노테이션이 붙은 메서드는 각 테스트 메서드가 실행되기 전에 항상 실행
    // 테스트 실행전 SignupRequest 객체 초기화
    @BeforeEach
    void setUp() {
        // Given: 테스트에 필요한 기본적인 SignupRequest 객체 설정
        signupRequest = new SignupRequest("test@example.com", "password123", "USER");
    }

    @Test
    void signup_fails_when_email_already_exists() {

        // Given : 시나리오 진행에 필요한 값을 설정, 테스트의 상태를 설정
        // when(테스트 할 부위)
        // 조건문을 트루로 했을때를 테스트 하는것이므로 : .thenReturn(true)
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // When : 시나리오 진행 필요조건 명시, 테스트하고자 하는 행동
        // signup 메서드를 호출했을 때 InvalidRequestException이 발생하는지 확인
        // assertThrows 블록이 예외를 발생시키는지를 검증하는 데 사용
        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            authService.signup(signupRequest);
        });

        // Then : 시나리오를 완료했을 때 보장해야 하는 결과를 명시, 예상되는 변화 설명
        // assertEquals 두 값이 동일한지 비교 (exception에 있는 메세지와 비교)
        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());

        // passwordEncoder.encode() 메서드가 호출되지 않았음을 확인
        verify(passwordEncoder, never()).encode(anyString());

        // userRepository.save() 메서드가 호출되지 않았음을 확인? 검증 (회원가입이 실패했으므로)
        verify(userRepository, never()).save(any(User.class));

        // jwtUtil.createToken() 메서드가 호출되지 않았음을 확인
        verify(jwtUtil, never()).createToken(anyLong(), anyString(), any(UserRole.class));

    }
}
