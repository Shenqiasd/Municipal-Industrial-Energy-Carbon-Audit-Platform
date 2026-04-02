package com.energy.audit.service.setting.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.setting.BsEnergyMapper;
import com.energy.audit.dao.mapper.setting.BsUnitEnergyMapper;
import com.energy.audit.dao.mapper.setting.BsUnitMapper;
import com.energy.audit.model.entity.setting.BsUnit;
import com.energy.audit.model.entity.setting.BsUnitEnergy;
import com.energy.audit.service.setting.UnitSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Unit setting service implementation
 */
@Service
public class UnitSettingServiceImpl implements UnitSettingService {

    private final BsUnitMapper unitMapper;
    private final BsUnitEnergyMapper unitEnergyMapper;
    private final BsEnergyMapper energyMapper;

    public UnitSettingServiceImpl(BsUnitMapper unitMapper,
                                   BsUnitEnergyMapper unitEnergyMapper,
                                   BsEnergyMapper energyMapper) {
        this.unitMapper = unitMapper;
        this.unitEnergyMapper = unitEnergyMapper;
        this.energyMapper = energyMapper;
    }

    @Override
    public BsUnit getById(Long id) {
        BsUnit unit = unitMapper.selectById(id);
        if (unit == null) {
            throw new BusinessException("Unit not found: " + id);
        }
        return unit;
    }

    @Override
    public List<BsUnit> list(BsUnit query) {
        if (query.getEnterpriseId() == null) {
            query.setEnterpriseId(SecurityUtils.getCurrentEnterpriseId());
        }
        return unitMapper.selectList(query);
    }

    @Override
    @Transactional
    public void create(BsUnit unit) {
        String operator = SecurityUtils.getCurrentUsername();
        Long enterpriseId = SecurityUtils.getCurrentEnterpriseId();
        unit.setCreateBy(operator);
        unit.setUpdateBy(operator);
        if (unit.getEnterpriseId() == null) {
            unit.setEnterpriseId(enterpriseId);
        }
        unitMapper.insert(unit);
    }

    @Override
    public void update(BsUnit unit) {
        getById(unit.getId());
        unit.setUpdateBy(SecurityUtils.getCurrentUsername());
        unitMapper.updateById(unit);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getById(id);
        String operator = SecurityUtils.getCurrentUsername();
        unitMapper.deleteById(id, operator);
        unitEnergyMapper.deleteAllByUnitId(id, operator);
    }

    @Override
    public List<BsUnitEnergy> getUnitEnergies(Long unitId) {
        return unitEnergyMapper.selectByUnitId(unitId);
    }

    @Override
    @Transactional
    public void addUnitEnergy(Long unitId, Long energyId) {
        getById(unitId);
        if (energyMapper.selectById(energyId) == null) {
            throw new BusinessException("Energy not found: " + energyId);
        }
        String operator = SecurityUtils.getCurrentUsername();
        BsUnitEnergy ue = new BsUnitEnergy();
        ue.setUnitId(unitId);
        ue.setEnergyId(energyId);
        ue.setCreateBy(operator);
        ue.setUpdateBy(operator);
        unitEnergyMapper.insert(ue);
    }

    @Override
    @Transactional
    public void removeUnitEnergy(Long unitId, Long energyId) {
        unitEnergyMapper.deleteByUnitIdAndEnergyId(unitId, energyId, SecurityUtils.getCurrentUsername());
    }
}
