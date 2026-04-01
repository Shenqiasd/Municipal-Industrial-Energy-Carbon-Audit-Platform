package com.energy.audit.model.entity.enterprise;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Enterprise entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntEnterprise extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Enterprise name */
    private String name;

    /** Unified social credit code */
    private String creditCode;

    /** Industry type */
    private String industryType;

    /** Province */
    private String province;

    /** City */
    private String city;

    /** District */
    private String district;

    /** Detailed address */
    private String address;

    /** Contact person */
    private String contactPerson;

    /** Contact phone */
    private String contactPhone;

    /** Contact email */
    private String contactEmail;

    /** Status (0=disabled, 1=enabled) */
    private Integer status;

    /** Remark */
    private String remark;
}
