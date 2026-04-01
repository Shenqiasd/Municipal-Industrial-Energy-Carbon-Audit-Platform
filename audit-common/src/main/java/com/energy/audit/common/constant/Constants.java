package com.energy.audit.common.constant;

/**
 * Common constants
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    /** User type: Administrator */
    public static final int USER_TYPE_ADMIN = 1;

    /** User type: Auditor */
    public static final int USER_TYPE_AUDITOR = 2;

    /** User type: Enterprise */
    public static final int USER_TYPE_ENTERPRISE = 3;

    /** Audit status: Pending */
    public static final int AUDIT_STATUS_PENDING = 0;

    /** Audit status: Approved */
    public static final int AUDIT_STATUS_APPROVED = 1;

    /** Audit status: Rejected */
    public static final int AUDIT_STATUS_REJECTED = 2;

    /** Logical delete: Not deleted */
    public static final int NOT_DELETED = 0;

    /** Logical delete: Deleted */
    public static final int DELETED = 1;

    /** Default page size */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** JWT token header */
    public static final String TOKEN_HEADER = "Authorization";

    /** JWT token prefix */
    public static final String TOKEN_PREFIX = "Bearer ";
}
