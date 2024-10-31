package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggerForAdminAop {
    @Pointcut("execution(* org.example.expert.domain.comment.controller.CommentAdminController.*(..))")
    private void delete() {}
    @Pointcut("execution(* org.example.expert.domain.user.controller.UserAdminController.*(..))")
    private void change() {}

    @Around("delete() || change()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        String decodedURI = URLDecoder.decode(request.getRequestURI(), "UTF-8");

        Object result = null;
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> params = new HashMap<>();

        try {
            params.put("http_method", request.getMethod());
            params.put("request_uri", decodedURI);
            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("params", getParams(request));
            params.put("log_time", System.currentTimeMillis());

            result = joinPoint.proceed(joinPoint.getArgs());

        } catch (Exception e) {
            log.error("LoggerAspect error", e);

        } finally {
            log.info("[{}] {}", params.get("http_method"), params.get("request_uri"));
            log.info("method: {}.{}", params.get("controller") ,params.get("method"));
            log.info("params: {}", params.get("params"));
            log.info("requestTime: {}", params.get("log_time"));

            log.info("response: {}", objectMapper.writeValueAsString(result));
        }
        return result;
    }

    private JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
