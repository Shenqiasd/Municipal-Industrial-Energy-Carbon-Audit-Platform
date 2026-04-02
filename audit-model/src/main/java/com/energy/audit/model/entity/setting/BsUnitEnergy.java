package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Unit-energy association entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsUnitEnergy extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long unitId;

    private Long energyId;

    private String energyName;

    private String measurementUnit;
}
