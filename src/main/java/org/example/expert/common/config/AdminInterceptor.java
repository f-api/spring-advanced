package org.example.expert.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.exception.AdminPrivilegeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {
    //로깅 객체
    private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);

    //요청 전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userRole = request.getHeader("User-Role");
        //엔드포인트에 ADMIN 이 아닌 권한의 유저가 접근할 경우
        if (!"ADMIN".equals(userRole)) {
            logger.warn("Unauthorized access attempt to {} at {}", request.getRequestURI(), LocalDateTime.now());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            throw new AdminPrivilegeException("Access denied: Admin privileges are required");
        }
        logger.info("Admin access granted to {} at {}", request.getRequestURI(), LocalDateTime.now());
        return true;
    }

    //요청 후
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Request processed for {} at {}", request.getRequestURI(), LocalDateTime.now());
    }

    //요청 완료 후
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //오류 발생한 경우
        if (ex != null) {
            logger.error("Exception occurred after processing request {}: {}", request.getRequestURI(), ex.getMessage());
        }
        logger.info("Request completed for {} at {}", request.getRequestURI(), LocalDateTime.now());
    }
}
