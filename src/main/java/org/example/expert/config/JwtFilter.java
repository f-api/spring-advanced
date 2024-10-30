package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

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

        if (bearerJwt == null) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        if (!jwtUtil.validateToken(jwt)) {
            log.error("Token Error");
            return;
        }

        Claims claims = jwtUtil.extractClaims(jwt);
        if (claims == null) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
            return;
        }

        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

        httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
        httpRequest.setAttribute("email", claims.get("email"));
        httpRequest.setAttribute("userRole", claims.get("userRole"));

        if (url.startsWith("/admin")) {
            if (!UserRole.ADMIN.equals(userRole)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
