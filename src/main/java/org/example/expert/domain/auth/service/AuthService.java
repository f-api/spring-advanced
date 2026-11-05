package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.JwtUtil;
import org.example.expert.common.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

		//1. 먼저 에메일 존재하는지 확인
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new InvalidRequestException("이미 존재하는 이메일입니다.");
		}

		//2. 비밀번호 encoding + User 생성
		UserRole userRole = UserRole.of(signupRequest.getUserRole());
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );

		//3. User 저장 + 토큰 생성
        User savedUser = userRepository.save(newUser);
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);
        return new SignupResponse(bearerToken);
    }

    @Transactional(readOnly = true)
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }
}
