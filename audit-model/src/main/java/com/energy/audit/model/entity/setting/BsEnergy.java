package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Energy type base setting entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsEnergy extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Energy code */
    private String energyCode;

    /** Energy name */
    private String energyName;

    /** Energy category */
    private String category;

    /** Unit */
    private String unit;

    /** Standard coal conversion factor */
    private BigDecimal conversionFactor;

    /** Carbon emission factor */
    private BigDecimal carbonFactor;

    /** Display order */
    private Integer orderNum;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;
}
