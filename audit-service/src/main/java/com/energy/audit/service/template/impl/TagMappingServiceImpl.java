package com.energy.audit.service.template.impl;

import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.template.TplTagMappingMapper;
import com.energy.audit.model.entity.template.TplTagMapping;
import com.energy.audit.service.template.SpreadsheetDataExtractor;
import com.energy.audit.service.template.TagMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TagMappingServiceImpl implements TagMappingService {

    private final TplTagMappingMapper tagMappingMapper;
    private final SpreadsheetDataExtractor extractor;

    public TagMappingServiceImpl(TplTagMappingMapper tagMappingMapper,
                                 SpreadsheetDataExtractor extractor) {
        this.tagMappingMapper = tagMappingMapper;
        this.extractor = extractor;
    }

    @Override
    @Transactional
    public void replaceAll(Long templateVersionId, List<TplTagMapping> mappings) {
        String operator = SecurityUtils.getRequiredCurrentUsername();
        tagMappingMapper.deleteByVersionId(templateVersionId, operator);
        if (mappings != null && !mappings.isEmpty()) {
            mappings.forEach(m -> {
                m.setTemplateVersionId(templateVersionId);
                m.setCreateBy(operator);
                m.setUpdateBy(operator);
            });
            tagMappingMapper.batchInsert(mappings);
        }
    }

    @Override
    public List<TplTagMapping> listByVersionId(Long templateVersionId) {
        return tagMappingMapper.selectListByVersionId(templateVersionId);
    }

    @Override
    @Transactional
    public void syncFromTemplateJson(Long versionId, String templateJson) {
        String operator = SecurityUtils.getRequiredCurrentUsername();
        Set<String> discoveredTags = extractor.discoverTagNames(templateJson);

        List<TplTagMapping> existing = tagMappingMapper.selectListByVersionId(versionId);

        for (String tagName : discoveredTags) {
            TplTagMapping found = tagMappingMapper.selectByVersionIdAndTagName(versionId, tagName);
            if (found == null) {
                TplTagMapping newMapping = new TplTagMapping();
                newMapping.setTemplateVersionId(versionId);
                newMapping.setTagName(tagName);
                // Use tagName as default fieldName to satisfy NOT NULL constraint;
                // admin can reconfigure via the tag-config panel.
                newMapping.setFieldName(tagName);
                newMapping.setDataType("STRING");
                newMapping.setRequired(0);
                newMapping.setSheetIndex(0);
                newMapping.setCreateBy(operator);
                newMapping.setUpdateBy(operator);
                tagMappingMapper.insert(newMapping);
            }
        }

        for (TplTagMapping m : existing) {
            if (!discoveredTags.contains(m.getTagName())) {
                tagMappingMapper.softDeleteById(m.getId(), operator);
            }
        }
    }
}
