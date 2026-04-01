package com.energy.audit.model.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity with common fields
 */
@Data
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Primary key */
    private Long id;

    /** Creator */
    private String createBy;

    /** Creation time */
    private LocalDateTime createTime;

    /** Updater */
    private String updateBy;

    /** Update time */
    private LocalDateTime updateTime;

    /** Logical delete flag (0=not deleted, 1=deleted) */
    private Integer deleted;
}
