package org.example.expert.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    private void changeUserRole() {
    }

    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    private void deleteComment() {
    }

    @Around("changeUserRole() || deleteComment()")
    public Object adminApiLog(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;

        String userId = request.getAttribute("userId").toString();
        String requestUrl = request.getRequestURL().toString();
        LocalDateTime requestTime = LocalDateTime.now();
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("======== [Request] ========");
        log.info("요청한 사용자의 ID : {}", userId);
        log.info("API 요청 URL : {}", requestUrl);
        log.info("API 요청 시각 : {}", requestTime);
        log.info("requestBody : {}", requestBody);
        log.info("============================");

        Object response = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(response);

        log.info("======== [Response] ========");
        log.info("Response Body: {}", responseBody);
        log.info("============================");

        return response;
    }
}
