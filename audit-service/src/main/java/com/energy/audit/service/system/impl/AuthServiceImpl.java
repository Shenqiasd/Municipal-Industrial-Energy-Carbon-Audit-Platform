package com.energy.audit.service.system.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.model.entity.system.SysUser;
import com.energy.audit.service.system.AuthService;
import com.energy.audit.service.system.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication service implementation
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final SysUserService sysUserService;

    public AuthServiceImpl(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            throw new BusinessException(401, "Invalid username or password");
        }
        // TODO: verify password with encoder
        // TODO: generate JWT token using JwtUtils
        Map<String, Object> result = new HashMap<>();
        result.put("token", "TODO_GENERATE_JWT_TOKEN");
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("userType", user.getUserType());
        return result;
    }

    @Override
    public void logout(String token) {
        // TODO: invalidate token (e.g., add to blacklist cache)
        log.info("User logged out, token invalidated");
    }

    @Override
    public Map<String, Object> refreshToken(String token) {
        // TODO: validate existing token, generate new token
        Map<String, Object> result = new HashMap<>();
        result.put("token", "TODO_REFRESHED_JWT_TOKEN");
        return result;
    }
}
