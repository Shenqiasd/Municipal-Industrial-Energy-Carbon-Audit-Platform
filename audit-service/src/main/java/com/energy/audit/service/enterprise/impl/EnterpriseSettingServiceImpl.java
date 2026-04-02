package com.energy.audit.service.enterprise.impl;

import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.enterprise.EntEnterpriseSettingMapper;
import com.energy.audit.model.entity.enterprise.EntEnterpriseSetting;
import com.energy.audit.service.enterprise.EnterpriseSettingService;
import org.springframework.stereotype.Service;

/**
 * Enterprise setting service implementation
 */
@Service
public class EnterpriseSettingServiceImpl implements EnterpriseSettingService {

    private final EntEnterpriseSettingMapper settingMapper;

    public EnterpriseSettingServiceImpl(EntEnterpriseSettingMapper settingMapper) {
        this.settingMapper = settingMapper;
    }

    @Override
    public EntEnterpriseSetting get(Long enterpriseId) {
        return settingMapper.selectByEnterpriseId(enterpriseId);
    }

    @Override
    public void save(EntEnterpriseSetting setting) {
        String operator = SecurityUtils.getCurrentUsername();
        if (setting.getCreateBy() == null) {
            setting.setCreateBy(operator);
        }
        setting.setUpdateBy(operator);
        settingMapper.upsert(setting);
    }
}
