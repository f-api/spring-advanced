package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service  // 이 클래스가 비즈니스 로직을 처리하는 Service 클래스임을 Spring에 등록
@RequiredArgsConstructor  // final 필드를 생성자 주입 방식으로 자동 생성
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public UserResponse getUser(long userId) {
		User user = findUserById(userId);
		return new UserResponse(user.getId(), user.getEmail());
	}

	@Transactional
	public void changePassword(long userId, UserChangePasswordRequest request) {
		User user = findUserById(userId);
		validateNewPasswordIsDifferent(request.getNewPassword(), user.getPassword());
		validateOldPasswordMatches(request.getOldPassword(), user.getPassword());
		user.changePassword(passwordEncoder.encode(request.getNewPassword()));
	}

	// private로 쓴 이유: UserService 내부에서만 사용하는 공통 조회 로직이므로 private으로 제한
	private User findUserById(long userId) {
		// userId로 유저를 조회하고, 없으면 예외 발생
		return userRepository.findById(userId)
			.orElseThrow(() -> new InvalidRequestException("User not found"));
	}

	private void validateNewPasswordIsDifferent(String newPassword, String currentPassword) {
		// 새 비밀번호가 현재 비밀번호와 같은지 검증
		if (passwordEncoder.matches(newPassword, currentPassword)) {
			throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
		}
	}

	private void validateOldPasswordMatches(String oldPassword, String currentPassword) {
		// 사용자가 입력한 기존 비밀번호가 DB에 저장된 현재 비밀번호와 일치하는지 검증
		if (!passwordEncoder.matches(oldPassword, currentPassword)) {
			throw new InvalidRequestException("잘못된 비밀번호입니다.");
		}
	}
}
