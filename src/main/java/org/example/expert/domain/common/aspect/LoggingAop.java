package org.example.expert.domain.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingAop {

    private final ObjectMapper objectMapper;

    @Around("execution(* org.example.expert.domain.admin.controller.AdminController.*(..))")
    public Object commentAop(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis(); // API 요청 시각

        ServletRequestAttributes attr =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        Object[] args = joinPoint.getArgs();
        String requestBody = objectMapper.writeValueAsString(args); // 요청 본문


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        Object result = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(result); // 응답 본문
        log.info(" 유저 아이디 : {}," +
                        " API 요청 시각 : {}" +
                        " API 요청 URL : {}" +
                        " 요청 본문 : {}" +
                        " 응답 본문 : {}",
                userId,
                start,
                request.getRequestURI(),
                requestBody,
                responseBody);

        return result;
    }
}