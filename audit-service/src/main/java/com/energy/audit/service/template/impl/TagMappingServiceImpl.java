package com.energy.audit.service.template.impl;

import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.template.TplTagMappingMapper;
import com.energy.audit.model.entity.template.TplTagMapping;
import com.energy.audit.service.template.SpreadsheetDataExtractor;
import com.energy.audit.service.template.TagMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagMappingServiceImpl implements TagMappingService {

    private static final Logger log = LoggerFactory.getLogger(TagMappingServiceImpl.class);

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

        // Load all existing active mappings in one query and index by tagName.
        // Use merge function to handle any duplicate tagName rows defensively;
        // in practice the unique-active-tag invariant should be maintained by the schema.
        Map<String, TplTagMapping> existingByTag = tagMappingMapper
                .selectListByVersionId(versionId)
                .stream()
                .collect(Collectors.toMap(
                        TplTagMapping::getTagName,
                        m -> m,
                        (first, dup) -> {
                            log.warn("syncFromTemplateJson: duplicate active tag '{}' for versionId={}, keeping first",
                                    first.getTagName(), versionId);
                            return first;
                        }
                ));

        // Insert tags discovered in JSON but absent from DB
        for (String tagName : discoveredTags) {
            if (!existingByTag.containsKey(tagName)) {
                TplTagMapping newMapping = new TplTagMapping();
                newMapping.setTemplateVersionId(versionId);
                newMapping.setTagName(tagName);
                // Default fieldName = tagName to satisfy NOT NULL constraint;
                // admin reconfigures the actual field name via the tag-config panel.
                newMapping.setFieldName(tagName);
                newMapping.setDataType("STRING");
                newMapping.setRequired(0);
                newMapping.setSheetIndex(0);
                newMapping.setCreateBy(operator);
                newMapping.setUpdateBy(operator);
                tagMappingMapper.insert(newMapping);
            }
        }

        // Soft-delete tags that are no longer present in the JSON
        existingByTag.forEach((tagName, mapping) -> {
            if (!discoveredTags.contains(tagName)) {
                tagMappingMapper.softDeleteById(mapping.getId(), operator);
            }
        });
    }
}
