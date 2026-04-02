package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * Global energy catalog entity — admin-managed reference list (no enterprise_id).
 * Enterprises select from this catalog to populate their own bs_energy records.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsEnergyCatalog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Energy name */
    private String name;

    /** Energy category (dictionary: energy_category) */
    private String category;

    /** Measurement unit */
    private String measurementUnit;

    /** Standard coal equivalent value (折标系数等价值) */
    private BigDecimal equivalentValue;

    /** Equal value (折标系数当量值) */
    private BigDecimal equalValue;

    /** Low heat value */
    private BigDecimal lowHeatValue;

    /** Carbon content per unit heat value */
    private BigDecimal carbonContent;

    /** Oxidation rate */
    private BigDecimal oxidationRate;

    /** Display color */
    private String color;

    /** Active flag (0=inactive, 1=active) */
    private Integer isActive;

    /** Sort order */
    private Integer sortOrder;

    /** Remark */
    private String remark;
}
