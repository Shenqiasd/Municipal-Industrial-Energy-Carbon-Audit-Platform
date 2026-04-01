package com.energy.audit.web.interceptor;

import com.energy.audit.common.constant.Constants;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.web.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

/**
 * JWT authentication interceptor
 */
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthInterceptor.class);

    private final SecurityConfig securityConfig;
    private final ObjectMapper objectMapper;

    public JwtAuthInterceptor(SecurityConfig securityConfig, ObjectMapper objectMapper) {
        this.securityConfig = securityConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(Constants.TOKEN_HEADER);
        if (authHeader == null || !authHeader.startsWith(Constants.TOKEN_PREFIX)) {
            sendUnauthorized(response, "Missing or invalid Authorization header");
            return false;
        }

        String token = authHeader.substring(Constants.TOKEN_PREFIX.length());
        if (!securityConfig.validateToken(token)) {
            sendUnauthorized(response, "Invalid or expired token");
            return false;
        }

        try {
            Claims claims = securityConfig.parseToken(token);
            Long userId = claims.get("userId", Long.class);
            String username = claims.getSubject();
            Integer userType = claims.get("userType", Integer.class);

            SecurityUtils.setCurrentUserId(userId);
            SecurityUtils.setCurrentUsername(username);
            SecurityUtils.setCurrentUserType(userType);

            return true;
        } catch (Exception e) {
            log.error("Failed to parse JWT token", e);
            sendUnauthorized(response, "Token parsing failed");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityUtils.clear();
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of("code", 401, "message", message)
        ));
    }
}
