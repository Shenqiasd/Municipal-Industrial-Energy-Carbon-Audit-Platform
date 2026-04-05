package com.energy.audit.model.entity.enterprise;

import com.energy.audit.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Enterprise detailed setting entity — matches ent_enterprise_setting production schema.
 * One row per enterprise (unique on enterprise_id).
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntEnterpriseSetting extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** FK -> ent_enterprise.id (unique) */
    private Long enterpriseId;

    /** Enterprise address */
    private String enterpriseAddress;

    /** Unit address */
    private String unitAddress;

    /** Postal code */
    private String postalCode;

    /** Fax */
    private String fax;

    /** Legal representative name */
    private String legalRepresentative;

    /** Legal representative phone */
    private String legalPhone;

    /** Enterprise contact person */
    private String enterpriseContact;

    /** Enterprise contact mobile */
    private String enterpriseMobile;

    /** Enterprise contact email */
    private String enterpriseEmail;

    /** Report compiler contact person */
    private String compilerContact;

    /** Report compiler name */
    private String compilerName;

    /** Report compiler mobile */
    private String compilerMobile;

    /** Report compiler email */
    private String compilerEmail;

    /** Energy management certificate (0=no, 1=yes) */
    private Integer energyCert;

    /** Certificate authority */
    private String certAuthority;

    /** Certificate pass date */
    private LocalDate certPassDate;

    /** Registered capital (万元) */
    private BigDecimal registeredCapital;

    /** Registration date */
    private LocalDate registeredDate;

    /** Industry category */
    private String industryCategory;

    /** Industry code */
    private String industryCode;

    /** Industry name */
    private String industryName;

    /** Superior department */
    private String superiorDepartment;

    /** Unit nature (国有/私营/合资/外资/…) */
    private String unitNature;

    /** Energy enterprise type */
    private String energyEnterpriseType;

    /** Remark */
    private String remark;
}
