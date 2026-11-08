package org.example.expert.common;

import java.time.LocalDateTime;

import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogResponse {
	//========================== 관리자 API 접근 로그 ======================================
	// 접근 성공
	public void adminApiSuccess(Long userId, UserRole role) {
		log.info("관리자 API 접근 - Time: {}, UserId: {}, Role: {}", LocalDateTime.now(), userId, role);
	}

	// 접근 실패
	public void adminApiFail(String reason, Long userId, UserRole role) {
		log.warn("관리자 API 접근 거부 - Time: {}, UserId: {}, Role: {}, Reason: {}", LocalDateTime.now(), userId, role, reason);
	}
	//====================================================================================


	//========================== JWT 인증 로그 ======================================
	// 요청 처리 실패
	public void jwtFail(String reason, String url) {
		log.warn("요청 처리 실패 - Reason: {}, URI: {}", reason, url);
	}

	// JWT 만료 로그
	public void jwtExpired(String userId, String url) {
		log.info("JWT 만료: userId={}, URI={}", userId, url);
	}

	// JWT 예외 로그
	public void logError(String exceptionName, String url, Exception e) {
		log.error("요청 처리 중 오류 [{}]: URI={}", exceptionName, url, e);
	}
	//====================================================================================



}
