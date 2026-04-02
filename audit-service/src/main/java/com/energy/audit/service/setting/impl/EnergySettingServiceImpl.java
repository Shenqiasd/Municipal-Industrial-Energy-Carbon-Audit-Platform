package com.energy.audit.service.setting.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.setting.BsEnergyCatalogMapper;
import com.energy.audit.dao.mapper.setting.BsEnergyMapper;
import com.energy.audit.model.entity.setting.BsEnergy;
import com.energy.audit.model.entity.setting.BsEnergyCatalog;
import com.energy.audit.service.setting.EnergySettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Energy setting service implementation
 */
@Service
public class EnergySettingServiceImpl implements EnergySettingService {

    private static final Logger log = LoggerFactory.getLogger(EnergySettingServiceImpl.class);

    private final BsEnergyMapper energyMapper;
    private final BsEnergyCatalogMapper catalogMapper;

    public EnergySettingServiceImpl(BsEnergyMapper energyMapper, BsEnergyCatalogMapper catalogMapper) {
        this.energyMapper = energyMapper;
        this.catalogMapper = catalogMapper;
    }

    @Override
    @Cacheable(cacheNames = "energyCache", key = "#id")
    public BsEnergy getById(Long id) {
        BsEnergy energy = energyMapper.selectById(id);
        if (energy == null) {
            throw new BusinessException("Energy setting not found: " + id);
        }
        return energy;
    }

    @Override
    public List<BsEnergy> list(BsEnergy query) {
        if (query.getEnterpriseId() == null) {
            query.setEnterpriseId(SecurityUtils.getCurrentEnterpriseId());
        }
        return energyMapper.selectList(query);
    }

    @Override
    @CacheEvict(cacheNames = "energyCache", allEntries = true)
    public void create(BsEnergy energy) {
        String operator = SecurityUtils.getCurrentUsername();
        Long enterpriseId = SecurityUtils.getCurrentEnterpriseId();
        energy.setCreateBy(operator);
        energy.setUpdateBy(operator);
        if (energy.getEnterpriseId() == null) {
            energy.setEnterpriseId(enterpriseId);
        }
        if (energy.getIsActive() == null) {
            energy.setIsActive(1);
        }
        energyMapper.insert(energy);
    }

    @Override
    @CacheEvict(cacheNames = "energyCache", key = "#energy.id")
    public void update(BsEnergy energy) {
        getById(energy.getId());
        energy.setUpdateBy(SecurityUtils.getCurrentUsername());
        energyMapper.updateById(energy);
    }

    @Override
    @CacheEvict(cacheNames = "energyCache", key = "#id")
    public void delete(Long id) {
        getById(id);
        energyMapper.deleteById(id, SecurityUtils.getCurrentUsername());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "energyCache", allEntries = true)
    public void importFromCatalog(List<Long> catalogIds) {
        if (catalogIds == null || catalogIds.isEmpty()) {
            return;
        }
        String operator = SecurityUtils.getCurrentUsername();
        Long enterpriseId = SecurityUtils.getCurrentEnterpriseId();
        if (enterpriseId == null) {
            throw new BusinessException("Enterprise context not found — must be called by an enterprise user");
        }
        for (Long catalogId : catalogIds) {
            BsEnergyCatalog catalog = catalogMapper.selectById(catalogId);
            if (catalog == null) {
                log.warn("Catalog entry {} not found, skipping", catalogId);
                continue;
            }
            BsEnergy energy = new BsEnergy();
            energy.setEnterpriseId(enterpriseId);
            energy.setName(catalog.getName());
            energy.setCategory(catalog.getCategory());
            energy.setMeasurementUnit(catalog.getMeasurementUnit());
            energy.setEquivalentValue(catalog.getEquivalentValue());
            energy.setEqualValue(catalog.getEqualValue());
            energy.setLowHeatValue(catalog.getLowHeatValue());
            energy.setCarbonContent(catalog.getCarbonContent());
            energy.setOxidationRate(catalog.getOxidationRate());
            energy.setColor(catalog.getColor());
            energy.setIsActive(1);
            energy.setCreateBy(operator);
            energy.setUpdateBy(operator);
            energyMapper.insert(energy);
        }
    }
}
