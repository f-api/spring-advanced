package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggerForAdmin {
    @Pointcut("execution(* org.example.expert.domain.comment.controller.*(..))")
    private void delete() {}
    @Pointcut("execution(* org.example.expert.domain.user.controller.*(..))")
    private void change() {}

    @Around("delete() || change()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("관리자만 접근 가능한 메서드를 실행합니다.");

        try {
            return joinPoint.proceed();
        } finally {
            log.info("관리자만 접근 가능한 메서드를 종료합니다.");
        }
    }
}
