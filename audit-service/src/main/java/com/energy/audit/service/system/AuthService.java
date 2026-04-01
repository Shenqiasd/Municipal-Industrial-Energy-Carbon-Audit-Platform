package com.energy.audit.service.system;

import java.util.Map;

/**
 * Authentication service interface
 */
public interface AuthService {

    /**
     * Login with username and password
     * @param username the username
     * @param password the password
     * @return token information map containing "token" key
     */
    Map<String, Object> login(String username, String password);

    /**
     * Logout current user
     * @param token the JWT token
     */
    void logout(String token);

    /**
     * Refresh an existing token
     * @param token the existing JWT token
     * @return new token information
     */
    Map<String, Object> refreshToken(String token);
}
