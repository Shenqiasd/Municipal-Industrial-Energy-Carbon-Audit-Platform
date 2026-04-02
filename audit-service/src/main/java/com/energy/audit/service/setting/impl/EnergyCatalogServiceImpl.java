package com.energy.audit.service.setting.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.setting.BsEnergyCatalogMapper;
import com.energy.audit.model.entity.setting.BsEnergyCatalog;
import com.energy.audit.service.setting.EnergyCatalogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Global energy catalog service implementation
 */
@Service
public class EnergyCatalogServiceImpl implements EnergyCatalogService {

    private final BsEnergyCatalogMapper catalogMapper;

    public EnergyCatalogServiceImpl(BsEnergyCatalogMapper catalogMapper) {
        this.catalogMapper = catalogMapper;
    }

    @Override
    public BsEnergyCatalog getById(Long id) {
        BsEnergyCatalog catalog = catalogMapper.selectById(id);
        if (catalog == null) {
            throw new BusinessException("Energy catalog entry not found: " + id);
        }
        return catalog;
    }

    @Override
    public List<BsEnergyCatalog> list(BsEnergyCatalog query) {
        return catalogMapper.selectList(query);
    }

    @Override
    public void create(BsEnergyCatalog catalog) {
        String operator = SecurityUtils.getCurrentUsername();
        catalog.setCreateBy(operator);
        catalog.setUpdateBy(operator);
        if (catalog.getIsActive() == null) {
            catalog.setIsActive(1);
        }
        catalogMapper.insert(catalog);
    }

    @Override
    public void update(BsEnergyCatalog catalog) {
        getById(catalog.getId());
        catalog.setUpdateBy(SecurityUtils.getCurrentUsername());
        catalogMapper.updateById(catalog);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        catalogMapper.deleteById(id, SecurityUtils.getCurrentUsername());
    }
}
