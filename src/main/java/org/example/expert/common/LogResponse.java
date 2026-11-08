package org.example.expert.common;

import java.time.LocalDateTime;

import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogResponse {
	//========================== 관리자 API 접근 로그 ======================================
	// 관리자 API 접근 성공
	public void adminApiSuccess(Long userId, UserRole role) {
		log.info("관리자 API 접근 - Time: {}, UserId: {}, Role: {}", LocalDateTime.now(), userId, role);
	}

	// 관리자 API 접근 실패
	public void adminApiFail(String reason, Long userId, UserRole role) {
		log.warn("관리자 API 접근 거부 - Time: {}, UserId: {}, Role: {}, Reason: {}", LocalDateTime.now(), userId, role, reason);
	}

	// 인증되지 않은 사용자 접근
	public void adminApiUnauthenticated() {
		log.warn("관리자 API 접근 거부 - Time: {}, Reason: 인증되지 않은 사용자", LocalDateTime.now());
	}
	//====================================================================================


}
