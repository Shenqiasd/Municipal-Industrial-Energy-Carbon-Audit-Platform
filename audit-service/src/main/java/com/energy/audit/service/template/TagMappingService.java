package com.energy.audit.service.template;

import com.energy.audit.model.entity.template.TplTagMapping;

import java.util.List;

/**
 * Service for managing template tag mappings (tpl_tag_mapping)
 */
public interface TagMappingService {

    /** Replace all tag mappings for a version (soft-delete existing, batch-insert new) */
    void replaceAll(Long templateVersionId, List<TplTagMapping> mappings);

    /** Return all active mappings for a version */
    List<TplTagMapping> listByVersionId(Long templateVersionId);
}
