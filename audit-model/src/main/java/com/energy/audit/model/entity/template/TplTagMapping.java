package com.energy.audit.model.entity.template;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Template tag mapping entity - maps SpreadJS cell tags to data fields
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TplTagMapping extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Template version ID */
    private Long templateVersionId;

    /** Tag name */
    private String tagName;

    /** Target field name */
    private String fieldName;

    /** Target table name */
    private String targetTable;

    /** Data type (STRING/NUMBER/DATE/DICT) */
    private String dataType;

    /** Dict type (when dataType=DICT) */
    private String dictType;

    /** Required flag */
    private Integer required;

    /** Sheet index */
    private Integer sheetIndex;

    /** Cell range (e.g. A1:B10) */
    private String cellRange;

    /** Remark */
    private String remark;
}
