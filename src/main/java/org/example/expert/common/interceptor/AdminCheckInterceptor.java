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

		//1.login user 꺼내기
		Object userRole = request.getAttribute("userRole");

		//2.로그인 여부 확인
		if (userRole == null) {
			log.info("인증되지 않은 사용자입니다. 접근 거부");

			errorResponse.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");

			return false;
		}

		//3. 관리자 권한 확인
		UserRole role = UserRole.valueOf(userRole.toString());
		if(!role.equals(UserRole.ADMIN))
		{
			log.info("관리자 권한이 없습니다. 접근 거부");
			errorResponse.sendErrorResponse(response, HttpStatus.FORBIDDEN,"관리자만 접근할 수 있습니다." );

			return false;
		}

		//4. 로그 기록
		log.info("관리자 API 접근 - Time : {} - URL : {}", LocalDateTime.now(), request.getRequestURI());

		return true;

	}
}
