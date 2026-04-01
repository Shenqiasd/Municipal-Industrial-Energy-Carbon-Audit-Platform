package com.energy.audit.model.entity.system;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * System user entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Username */
    private String username;

    /** Password (encrypted) */
    private String password;

    /** Real name */
    private String realName;

    /** Phone number */
    private String phone;

    /** Email */
    private String email;

    /** User type (1=admin, 2=auditor, 3=enterprise) */
    private Integer userType;

    /** Role ID */
    private Long roleId;

    /** Enterprise ID (for enterprise users) */
    private Long enterpriseId;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;

    /** Avatar URL */
    private String avatar;
}
