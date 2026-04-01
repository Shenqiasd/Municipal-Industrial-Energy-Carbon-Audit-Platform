package com.energy.audit.common.enums;

import lombok.Getter;

/**
 * Audit status enumeration
 */
@Getter
public enum AuditStatus {

    PENDING(0, "Pending"),
    APPROVED(1, "Approved"),
    REJECTED(2, "Rejected");

    private final int code;
    private final String description;

    AuditStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AuditStatus fromCode(int code) {
        for (AuditStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown audit status code: " + code);
    }
}
