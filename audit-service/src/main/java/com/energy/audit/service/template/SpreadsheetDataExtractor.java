package com.energy.audit.service.template;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.model.entity.template.TplTagMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extracts structured data from SpreadJS JSON using tag mappings.
 *
 * SpreadJS JSON structure:
 *   Named ranges: root.names[] → { name, row, col, rowCount, colCount, sheetIndex }
 *   Cell tags:    root.sheets[sheetName].data.dataTable[row][col].tag → string
 */
@Component
public class SpreadsheetDataExtractor {

    private static final Logger log = LoggerFactory.getLogger(SpreadsheetDataExtractor.class);

    private final ObjectMapper objectMapper;

    public SpreadsheetDataExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Extract data from SpreadJS JSON based on tag/namedRange mappings.
     *
     * @param spreadjsJson the SpreadJS workbook JSON
     * @param tagMappings  the tag mapping definitions
     * @return extracted data as fieldName → value map
     */
    public Map<String, Object> extractData(String spreadjsJson, List<TplTagMapping> tagMappings) {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(spreadjsJson);
            JsonNode sheets = root.get("sheets");
            if (sheets == null || !sheets.isObject()) {
                log.warn("No sheets found in SpreadJS JSON");
                return result;
            }

            // Collect sheet names into a List once — preserves order and avoids
            // relying on repeated iterator traversal for sheetIndex resolution (fix C-1).
            List<String> sheetNameList = new ArrayList<>();
            sheets.fieldNames().forEachRemaining(sheetNameList::add);

            Map<String, JsonNode> namedRanges = parseNamedRanges(root);
            Map<String, JsonNode> cellTags = parseCellTags(sheets);

            for (TplTagMapping mapping : tagMappings) {
                Object value = null;
                String tagName = mapping.getTagName();

                if (namedRanges.containsKey(tagName)) {
                    value = extractFromNamedRange(sheets, sheetNameList, namedRanges.get(tagName));
                } else if (cellTags.containsKey(tagName)) {
                    value = extractCellValue(cellTags.get(tagName));
                }

                value = convertType(value, mapping.getDataType());

                // M-2: enforce required fields
                if (mapping.getRequired() != null && mapping.getRequired() == 1
                        && (value == null || value.toString().isBlank())) {
                    throw new BusinessException("必填字段 [" + mapping.getFieldName() + "] 未填写");
                }

                result.put(mapping.getFieldName(), value);
                log.debug("Extracted field: {} tag: {} value: {}", mapping.getFieldName(), tagName, value);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("Failed to extract data from SpreadJS JSON", e);
            throw new BusinessException("解析电子表格数据失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * Discover all tag names (cell tags + named ranges) in a SpreadJS workbook JSON.
     * Throws {@link BusinessException} on parse failure so callers can roll back.
     */
    public Set<String> discoverTagNames(String templateJson) {
        if (templateJson == null || templateJson.isBlank()) {
            throw new BusinessException("模板 JSON 不能为空");
        }
        try {
            JsonNode root = objectMapper.readTree(templateJson);
            Set<String> result = new HashSet<>();
            parseNamedRanges(root).keySet().forEach(result::add);
            JsonNode sheets = root.get("sheets");
            if (sheets != null && sheets.isObject()) {
                parseCellTags(sheets).keySet().forEach(result::add);
            }
            return result;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("discoverTagNames: failed to parse templateJson — {}", e.getMessage());
            throw new BusinessException("模板 JSON 解析失败，Tag 同步已中止: " + e.getMessage());
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private Map<String, JsonNode> parseNamedRanges(JsonNode root) {
        Map<String, JsonNode> map = new HashMap<>();
        JsonNode names = root.get("names");
        if (names != null && names.isArray()) {
            for (JsonNode nameNode : names) {
                String name = nameNode.has("name") ? nameNode.get("name").asText() : null;
                if (name != null && !name.isBlank()) {
                    map.put(name, nameNode);
                }
            }
        }
        return map;
    }

    private Map<String, JsonNode> parseCellTags(JsonNode sheets) {
        Map<String, JsonNode> map = new HashMap<>();
        sheets.fieldNames().forEachRemaining(sheetName -> {
            JsonNode dataTable = sheets.get(sheetName).path("data").path("dataTable");
            if (dataTable.isObject()) {
                dataTable.fields().forEachRemaining(rowEntry ->
                    rowEntry.getValue().fields().forEachRemaining(colEntry -> {
                        JsonNode cell = colEntry.getValue();
                        JsonNode tagNode = cell.get("tag");
                        if (tagNode == null) return;
                        String tagValue;
                        if (tagNode.isTextual()) {
                            // M-1: only accept plain-text tags; object tags are skipped
                            // (SpreadJS object tags should be stringified before being used as tag names)
                            tagValue = tagNode.asText();
                        } else {
                            log.debug("parseCellTags: non-text tag ignored — sheet={} type={}",
                                    sheetName, tagNode.getNodeType());
                            return;
                        }
                        if (!tagValue.isBlank()) {
                            map.put(tagValue, cell);
                        }
                    })
                );
            }
        });
        return map;
    }

    /**
     * Resolve a named range to its cell value.
     * Uses a pre-built List of sheet names so sheetIndex lookup is O(1) and
     * not sensitive to JSON field iteration order. (fix C-1)
     */
    private Object extractFromNamedRange(JsonNode sheets, List<String> sheetNameList, JsonNode rangeNode) {
        int sheetIdx = rangeNode.path("sheetIndex").asInt(0);
        int row = rangeNode.path("row").asInt(0);
        int col = rangeNode.path("col").asInt(0);

        if (sheetIdx < 0 || sheetIdx >= sheetNameList.size()) {
            log.warn("extractFromNamedRange: sheetIndex {} out of range (sheets={})", sheetIdx, sheetNameList.size());
            return null;
        }
        String sheetName = sheetNameList.get(sheetIdx);
        JsonNode cellNode = sheets.get(sheetName)
                .path("data").path("dataTable")
                .path(String.valueOf(row)).path(String.valueOf(col));
        return extractCellValue(cellNode);
    }

    /**
     * Extract the value from a SpreadJS cell node, preserving native numeric type
     * to avoid precision loss from double string-conversion. (fix N-2)
     */
    private Object extractCellValue(JsonNode cellNode) {
        JsonNode val = cellNode.path("value");
        if (val.isMissingNode() || val.isNull()) return null;
        if (val.isNumber()) return val.numberValue();
        return val.asText(null);
    }

    /**
     * Convert extracted string value to the target type declared in the tag mapping.
     * DICT: value is stored as-is (raw user input). Dict label→code translation is
     * out of scope for the extraction engine and should be handled at the reporting layer.
     */
    private Object convertType(Object value, String dataType) {
        if (value == null || dataType == null) return value;
        // If already a Number (from extractCellValue), only re-parse for STRING targets
        if (value instanceof Number && !"STRING".equalsIgnoreCase(dataType)) {
            Number num = (Number) value;
            return switch (dataType.toUpperCase()) {
                case "NUMBER" -> num;
                case "DATE"   -> num.toString();
                case "DICT"   -> num.toString();
                default       -> num.toString();
            };
        }
        String str = value.toString();
        try {
            return switch (dataType.toUpperCase()) {
                case "NUMBER" -> str.contains(".") ? Double.parseDouble(str) : Long.parseLong(str);
                case "DATE"   -> str;
                case "DICT"   -> str;   // C-2: DICT passes through as raw string
                default       -> str;
            };
        } catch (NumberFormatException e) {
            log.warn("Failed to convert value '{}' to type {}", str, dataType);
            return str;
        }
    }
}
