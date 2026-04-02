package com.energy.audit.model.entity.setting;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Unit-energy association entity — matches bs_unit_energy production schema.
 * Links a bs_unit to its associated bs_energy records.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BsUnitEnergy extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Unit ID -> bs_unit.id */
    private Long unitId;

    /** Energy ID -> bs_energy.id */
    private Long energyId;
}
