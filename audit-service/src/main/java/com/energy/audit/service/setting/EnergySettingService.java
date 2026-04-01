package com.energy.audit.service.setting;

import com.energy.audit.model.entity.setting.BsEnergy;

import java.util.List;

/**
 * Energy setting service interface
 */
public interface EnergySettingService {

    BsEnergy getById(Long id);

    List<BsEnergy> list(BsEnergy query);

    void create(BsEnergy energy);

    void update(BsEnergy energy);

    void delete(Long id);
}
