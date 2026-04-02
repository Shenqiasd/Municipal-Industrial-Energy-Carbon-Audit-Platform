package com.energy.audit.service.setting;

import com.energy.audit.model.entity.setting.BsEnergyCatalog;

import java.util.List;

/**
 * Global energy catalog service
 */
public interface EnergyCatalogService {

    BsEnergyCatalog getById(Long id);

    List<BsEnergyCatalog> list(BsEnergyCatalog query);

    void create(BsEnergyCatalog catalog);

    void update(BsEnergyCatalog catalog);

    void delete(Long id);
}
