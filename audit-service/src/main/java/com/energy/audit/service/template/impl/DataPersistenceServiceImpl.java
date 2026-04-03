package com.energy.audit.service.template.impl;

import com.energy.audit.common.util.SecurityUtils;
import com.energy.audit.dao.mapper.extraction.DeSubmissionFieldMapper;
import com.energy.audit.dao.mapper.extraction.DeSubmissionTableMapper;
import com.energy.audit.model.entity.extraction.DeSubmissionField;
import com.energy.audit.model.entity.extraction.DeSubmissionTable;
import com.energy.audit.model.entity.template.TplTagMapping;
import com.energy.audit.service.template.DataPersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataPersistenceServiceImpl implements DataPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(DataPersistenceServiceImpl.class);

    private final DeSubmissionFieldMapper fieldMapper;
    private final DeSubmissionTableMapper tableMapper;
    private final ObjectMapper objectMapper;

    public DataPersistenceServiceImpl(DeSubmissionFieldMapper fieldMapper,
                                       DeSubmissionTableMapper tableMapper,
                                       ObjectMapper objectMapper) {
        this.fieldMapper = fieldMapper;
        this.tableMapper = tableMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void persistExtractedData(Long submissionId, Long enterpriseId, Integer auditYear,
                                      Map<String, Object> extractedData,
                                      List<TplTagMapping> mappings) {
        String operator = SecurityUtils.getRequiredCurrentUsername();

        fieldMapper.deleteBySubmissionId(submissionId, operator);
        tableMapper.deleteBySubmissionId(submissionId, operator);

        List<DeSubmissionField> scalarFields = new ArrayList<>();
        List<DeSubmissionTable> tableRows = new ArrayList<>();

        for (TplTagMapping mapping : mappings) {
            String fieldName = mapping.getFieldName();
            Object value = extractedData.get(fieldName);
            if (value == null) continue;

            String mappingType = mapping.getMappingType() != null ? mapping.getMappingType() : "SCALAR";

            if ("TABLE".equalsIgnoreCase(mappingType) && value instanceof List) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> rows = (List<Map<String, Object>>) value;
                for (int i = 0; i < rows.size(); i++) {
                    Map<String, Object> row = rows.get(i);
                    DeSubmissionTable tableRow = new DeSubmissionTable();
                    tableRow.setSubmissionId(submissionId);
                    tableRow.setEnterpriseId(enterpriseId);
                    tableRow.setAuditYear(auditYear);
                    tableRow.setTagName(mapping.getTagName());
                    tableRow.setRowIndex(i);
                    Object rowKey = row.get("_rowKey");
                    tableRow.setRowKey(rowKey != null ? rowKey.toString() : null);
                    try {
                        tableRow.setColumnValues(objectMapper.writeValueAsString(row));
                    } catch (Exception e) {
                        log.warn("Failed to serialize table row for tag '{}', row {}", mapping.getTagName(), i);
                        tableRow.setColumnValues("{}");
                    }
                    tableRow.setCreateBy(operator);
                    tableRow.setUpdateBy(operator);
                    tableRows.add(tableRow);
                }
            } else {
                DeSubmissionField field = new DeSubmissionField();
                field.setSubmissionId(submissionId);
                field.setEnterpriseId(enterpriseId);
                field.setAuditYear(auditYear);
                field.setTagName(mapping.getTagName());
                field.setFieldName(fieldName);
                field.setCreateBy(operator);
                field.setUpdateBy(operator);

                if (value instanceof Number) {
                    field.setValueNumber(new BigDecimal(value.toString()));
                    field.setValueText(value.toString());
                } else {
                    field.setValueText(value.toString());
                }

                scalarFields.add(field);
            }
        }

        if (!scalarFields.isEmpty()) {
            fieldMapper.batchInsert(scalarFields);
            log.info("Persisted {} scalar fields for submission {}", scalarFields.size(), submissionId);
        }
        if (!tableRows.isEmpty()) {
            tableMapper.batchInsert(tableRows);
            log.info("Persisted {} table rows for submission {}", tableRows.size(), submissionId);
        }
    }
}
