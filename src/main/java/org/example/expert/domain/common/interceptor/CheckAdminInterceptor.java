package org.example.expert.domain.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        UserRole userRole = UserRole.of((String)request.getAttribute("userRole"));
        LocalDateTime reqTime = LocalDateTime.now();

        if(!(userRole == UserRole.ADMIN)) {
            log.warn("관리자 아님. 접근 거부");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 접근할 수 있습니다.");
            return false;
        }
        log.info("관리자 인증 성공, 요청 시각 {}, 요청 URL {}", reqTime, request.getRequestURL());

        return true;
    }
}
