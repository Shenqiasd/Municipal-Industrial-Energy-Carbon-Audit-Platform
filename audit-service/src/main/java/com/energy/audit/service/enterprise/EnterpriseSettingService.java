package com.energy.audit.service.enterprise;

import com.energy.audit.model.entity.enterprise.EntEnterpriseSetting;

/**
 * Enterprise setting service — one setting row per enterprise.
 */
public interface EnterpriseSettingService {

    EntEnterpriseSetting get(Long enterpriseId);

    void save(EntEnterpriseSetting setting);
}
