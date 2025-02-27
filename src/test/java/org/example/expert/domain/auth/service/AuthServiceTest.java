package org.example.expert.domain.auth.service;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Strict Stubbing 완화
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;


    //given 이메일이 이미 존재한다고 가정

    @BeforeEach
    void 사용자_정보_설정() {
        signupRequest = new SignupRequest("test@example.com", "Password123!", "USER");
    }

    @Test
    void 이메일_중복일_경우_비밀번호_암호화_호출되지_않음() {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

    // when
        assertThatThrownBy(() -> authService.signup(signupRequest))

    // then
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이미 존재하는 이메일입니다.");

        verify(passwordEncoder, never()).encode(anyString()); // passwordEncoder 호출되지 않아야 함
        verify(userRepository, times(1)).existsByEmail(signupRequest.getEmail()); // 이메일 체크 1번 호출됨
    }

    @Test
    void 정상적인_회원가입() {
    // given 이메일이 존재하지 않는 경우
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("hashedPassword");

        when(jwtUtil.createToken(any(), anyString(), any(UserRole.class))).thenReturn("mockedToken");

        User savedUser = new User(signupRequest.getEmail(), "hashedPassword", UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

    // when
        SignupResponse response = authService.signup(signupRequest);

    // then
        assertThat(response).isNotNull();
        assertThat(response.getBearerToken()).isEqualTo("mockedToken");

        verify(passwordEncoder, times(1)).encode(signupRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));

    }
}
