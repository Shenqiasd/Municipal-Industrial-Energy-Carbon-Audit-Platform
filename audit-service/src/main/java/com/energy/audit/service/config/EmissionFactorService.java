package com.energy.audit.service.config;

import com.energy.audit.model.entity.config.CmEmissionFactor;

import java.util.List;

/**
 * Carbon emission factor service
 */
public interface EmissionFactorService {

    CmEmissionFactor getById(Long id);

    List<CmEmissionFactor> list(CmEmissionFactor query);

    void create(CmEmissionFactor factor);

    void update(CmEmissionFactor factor);

    void delete(Long id);
}
