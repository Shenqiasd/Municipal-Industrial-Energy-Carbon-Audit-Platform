package com.energy.audit.model.entity.template;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Template edit lock entity - prevents concurrent editing
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TplEditLock extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Enterprise ID */
    private Long enterpriseId;

    /** Template ID */
    private Long templateId;

    /** Audit year */
    private Integer auditYear;

    /** Lock holder user ID */
    private Long lockUserId;

    /** Lock acquisition time */
    private LocalDateTime lockTime;

    /** Lock expiration time */
    private LocalDateTime expireTime;
}
