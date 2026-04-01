package com.energy.audit.common.util;

import com.energy.audit.common.exception.BusinessException;

/**
 * Security utility for managing current user context via ThreadLocal
 */
public final class SecurityUtils {

    private SecurityUtils() {
        // Prevent instantiation
    }

    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USERNAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> CURRENT_USER_TYPE = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static Long getCurrentUserId() {
        Long userId = CURRENT_USER_ID.get();
        if (userId == null) {
            throw new BusinessException(401, "User not authenticated");
        }
        return userId;
    }

    public static Long getCurrentUserIdOrNull() {
        return CURRENT_USER_ID.get();
    }

    public static void setCurrentUsername(String username) {
        CURRENT_USERNAME.set(username);
    }

    public static String getCurrentUsername() {
        String username = CURRENT_USERNAME.get();
        if (username == null) {
            throw new BusinessException(401, "User not authenticated");
        }
        return username;
    }

    public static void setCurrentUserType(Integer userType) {
        CURRENT_USER_TYPE.set(userType);
    }

    public static Integer getCurrentUserType() {
        return CURRENT_USER_TYPE.get();
    }

    public static void clear() {
        CURRENT_USER_ID.remove();
        CURRENT_USERNAME.remove();
        CURRENT_USER_TYPE.remove();
    }
}
