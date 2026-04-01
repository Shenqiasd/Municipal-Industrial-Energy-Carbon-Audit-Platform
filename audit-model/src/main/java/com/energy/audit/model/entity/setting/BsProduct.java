package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Product base setting entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsProduct extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Product code */
    private String productCode;

    /** Product name */
    private String productName;

    /** Product category */
    private String category;

    /** Unit of measurement */
    private String unit;

    /** Display order */
    private Integer orderNum;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;

    /** Remark */
    private String remark;
}
