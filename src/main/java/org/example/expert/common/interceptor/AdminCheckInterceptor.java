package org.example.expert.common.interceptor;

import java.io.IOException;
import java.time.LocalDateTime;

import org.example.expert.common.exception.ErrorResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

	private final ErrorResponse errorResponse;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		IOException {


		String method = request.getMethod();
		String uri = request.getRequestURI();
		Object userId = request.getAttribute("userId");
		Object userRole = request.getAttribute("userRole");
		LocalDateTime now = LocalDateTime.now();

		//1.로그인 여부 확인
		if (userRole == null) {
			log.warn("관리자 API 접근 거부 - Time: {}, Method: {}, URL: {}, UserId: {}, Reason: 인증되지 않은 사용자", now, method, uri, userId);
			errorResponse.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");

			return false;
		}

		//2. 관리자 권한 확인
		UserRole role = UserRole.valueOf(userRole.toString());
		if(!role.equals(UserRole.ADMIN))
		{
			log.warn("관리자 API 접근 거부 - Time: {}, Method: {}, URL: {}, UserId: {}, Role: {}, Reason: 관리자 권한 없음", now, method, uri, userId, role);
			errorResponse.sendErrorResponse(response, HttpStatus.FORBIDDEN,"관리자만 접근할 수 있습니다." );

			return false;
		}

		//3. 로그 기록
		log.info("관리자 API 접근 - Time: {}, Method: {}, URL: {}, UserId: {}, Role: {}", now, method, uri, userId, role);

		return true;

	}
}
