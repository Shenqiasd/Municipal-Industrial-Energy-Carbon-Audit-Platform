package com.energy.audit.model.entity.config;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Carbon emission factor entity — matches cm_emission_factor production schema.
 * Admin-managed global reference table.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CmEmissionFactor extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Factor name */
    private String factorName;

    /** Energy type */
    private String energyType;

    /** Emission factor value */
    private BigDecimal factorValue;

    /** Measurement unit */
    private String measurementUnit;

    /** Data source (standard / document name) */
    private String source;

    /** Effective year */
    private Integer effectiveYear;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;

    /** Remark */
    private String remark;
}
