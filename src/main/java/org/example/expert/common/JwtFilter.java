package org.example.expert.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.expert.common.exception.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final ErrorResponse errorResponse;
	private final LogResponse logResponse;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
			logResponse.jwtFail("인증 헤더 누락", url);
            errorResponse.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
				logResponse.jwtFail("Claims 추출 실패", url);
                errorResponse.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
                return;
            }

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logResponse.jwtExpired(e.getClaims().getSubject(), url);
            errorResponse.sendErrorResponse(httpResponse, HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
			logResponse.logError(e.getClass().getSimpleName(), url, e);
			errorResponse.sendErrorResponse(httpResponse, HttpStatus.BAD_REQUEST, "인증이 필요합니다.");
        } catch (Exception e) {
			logResponse.logError(e.getClass().getSimpleName(), url, e);
			errorResponse.sendErrorResponse(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR, "요청 처리 중 오류가 발생했습니다.");
        }
    }

}
