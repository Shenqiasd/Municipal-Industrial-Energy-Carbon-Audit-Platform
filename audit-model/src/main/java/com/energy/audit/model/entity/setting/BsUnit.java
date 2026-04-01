package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Unit base setting entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsUnit extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Unit code */
    private String unitCode;

    /** Unit name */
    private String unitName;

    /** Unit symbol */
    private String symbol;

    /** Unit category (e.g., energy, mass, volume) */
    private String category;

    /** Conversion factor to base unit */
    private BigDecimal conversionFactor;

    /** Display order */
    private Integer orderNum;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;
}
