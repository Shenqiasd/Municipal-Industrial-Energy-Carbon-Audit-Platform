package com.energy.audit.model.entity.template;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Template version entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TplTemplateVersion extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Template ID */
    private Long templateId;

    /** Version number */
    private Integer versionNum;

    /** SpreadJS JSON content */
    private String spreadjsJson;

    /** Version description */
    private String description;

    /** Status (0=draft, 1=published) */
    private Integer status;
}
