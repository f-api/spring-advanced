package org.example.expert.common.interceptor;

import java.io.IOException;

import org.example.expert.common.LogResponse;
import org.example.expert.common.exception.ErrorResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

	private final ErrorResponse errorResponse;
	private final LogResponse logResponse;
	private final UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		IOException {

		Object id = request.getAttribute("userId");

		//1. 인증 여부 확인
		if (id == null) {
			logResponse.adminApiUnauthenticated();
			errorResponse.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
			return false;
		}
		Long userId = (Long) id;


		//2. 관리자 권한 확인
		 UserRole userRole = userService.getUser(userId).getUserRole();
		if(userRole != UserRole.ADMIN)
		{
			logResponse.adminApiFail("관리자 권한 없음", userId, userRole);
			errorResponse.sendErrorResponse(response, HttpStatus.FORBIDDEN,"관리자만 접근할 수 있습니다." );
			return false;
		}

		//3. 관리자 API 접근 성공 로그 기록
		logResponse.adminApiSuccess(userId, userRole);

		return true;
	}
}
