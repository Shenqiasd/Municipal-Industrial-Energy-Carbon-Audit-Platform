package com.energy.audit.service.template;

import com.energy.audit.model.entity.template.TplTemplate;

import java.util.List;

/**
 * Template service interface
 */
public interface TemplateService {

    TplTemplate getById(Long id);

    TplTemplate getByCode(String templateCode);

    List<TplTemplate> list(TplTemplate query);

    void create(TplTemplate template);

    void update(TplTemplate template);

    void delete(Long id);

    /**
     * Publish a template (create a new version and set status to published)
     * @param templateId the template ID
     */
    void publish(Long templateId);
}
