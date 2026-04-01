package com.energy.audit.model.entity.template;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Template submission entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TplSubmission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Enterprise ID */
    private Long enterpriseId;

    /** Template version ID */
    private Long templateVersionId;

    /** SpreadJS JSON data (filled by enterprise) */
    private String spreadjsData;

    /** Audit year */
    private Integer auditYear;

    /** Submission status (0=draft, 1=submitted, 2=approved, 3=rejected) */
    private Integer status;

    /** Auditor ID */
    private Long auditorId;

    /** Audit opinion */
    private String auditOpinion;

    /** Remark */
    private String remark;
}
