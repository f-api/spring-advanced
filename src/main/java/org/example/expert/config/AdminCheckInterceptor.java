package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override

    // 역할 가져오기
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Object Role = request.getAttribute("userRole");

        // 역할이랑 일치하는지 확인
        if (Role == null || !"Admin".equals(Role.toString())) {
            throw new InvalidRequestException(" 어드민 권한이 필요합니다.");
        }


        //로깅 인포 추가!
        log.info("인증된 관리자 접근, Time:{}, URL:{}",
                LocalDateTime.now(), request.getRequestURL());

        // 특이사항 없으면 true
        return true;
    }
}
