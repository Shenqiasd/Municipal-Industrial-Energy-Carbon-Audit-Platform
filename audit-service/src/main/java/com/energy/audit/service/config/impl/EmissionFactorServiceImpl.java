package com.energy.audit.service.config.impl;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.config.CmEmissionFactorMapper;
import com.energy.audit.model.entity.config.CmEmissionFactor;
import com.energy.audit.service.config.EmissionFactorService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Carbon emission factor service implementation
 */
@Service
public class EmissionFactorServiceImpl implements EmissionFactorService {

    private final CmEmissionFactorMapper factorMapper;

    public EmissionFactorServiceImpl(CmEmissionFactorMapper factorMapper) {
        this.factorMapper = factorMapper;
    }

    @Override
    public CmEmissionFactor getById(Long id) {
        CmEmissionFactor factor = factorMapper.selectById(id);
        if (factor == null) {
            throw new BusinessException("Emission factor not found: " + id);
        }
        return factor;
    }

    @Override
    public List<CmEmissionFactor> list(CmEmissionFactor query) {
        return factorMapper.selectList(query);
    }

    @Override
    public void create(CmEmissionFactor factor) {
        String operator = SecurityUtils.getCurrentUsername();
        factor.setCreateBy(operator);
        factor.setUpdateBy(operator);
        if (factor.getStatus() == null) {
            factor.setStatus(1);
        }
        factorMapper.insert(factor);
    }

    @Override
    public void update(CmEmissionFactor factor) {
        getById(factor.getId());
        factor.setUpdateBy(SecurityUtils.getCurrentUsername());
        factorMapper.updateById(factor);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        factorMapper.deleteById(id, SecurityUtils.getCurrentUsername());
    }
}
