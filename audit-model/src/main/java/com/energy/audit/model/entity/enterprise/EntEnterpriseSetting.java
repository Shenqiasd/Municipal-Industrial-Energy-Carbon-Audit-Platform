package com.energy.audit.model.entity.enterprise;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Enterprise setting entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntEnterpriseSetting extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Enterprise ID */
    private Long enterpriseId;

    /** Setting key */
    private String settingKey;

    /** Setting value */
    private String settingValue;

    /** Annual energy consumption (tce) */
    private BigDecimal annualEnergyConsumption;

    /** Annual output value */
    private BigDecimal annualOutputValue;

    /** Audit year */
    private Integer auditYear;

    /** Remark */
    private String remark;
}
