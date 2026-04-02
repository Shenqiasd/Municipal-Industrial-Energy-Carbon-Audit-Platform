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

    /**
     * Sync tpl_tag_mapping from the SpreadJS template JSON:
     * - New tags discovered in JSON but absent from DB → insert with defaults
     * - Existing tags in DB → keep (preserve admin configuration)
     * - Tags in DB no longer in JSON → soft-delete
     */
    void syncFromTemplateJson(Long versionId, String templateJson);
}
