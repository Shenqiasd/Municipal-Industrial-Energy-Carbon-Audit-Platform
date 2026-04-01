package com.energy.audit.common.enums;

import lombok.Getter;

/**
 * User type enumeration
 */
@Getter
public enum UserType {

    ADMIN(1, "Administrator"),
    AUDITOR(2, "Auditor"),
    ENTERPRISE(3, "Enterprise");

    private final int code;
    private final String description;

    UserType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserType fromCode(int code) {
        for (UserType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user type code: " + code);
    }
}
