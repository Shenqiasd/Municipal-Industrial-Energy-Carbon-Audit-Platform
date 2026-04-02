package com.energy.audit.service.template;

import com.energy.audit.common.exception.BusinessException;
import com.energy.audit.model.entity.template.TplTagMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extracts structured data from SpreadJS JSON using tag mappings
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
     * SpreadJS JSON structure for named ranges:
     *   root.names[] -> { name, row, col, rowCount, colCount, sheetIndex }
     * SpreadJS JSON structure for cell tags:
     *   root.sheets[sheetName].data.dataTable[row][col].tag -> user-defined tag string
     *
     * @param spreadjsJson the SpreadJS workbook JSON
     * @param tagMappings  the tag mapping definitions
     * @return extracted data as fieldName -> value map
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

            Map<String, JsonNode> namedRanges = parseNamedRanges(root);
            Map<String, JsonNode> cellTags = parseCellTags(sheets);

            for (TplTagMapping mapping : tagMappings) {
                Object value = null;
                String tagName = mapping.getTagName();

                // Try named range first, then cell tag
                if (namedRanges.containsKey(tagName)) {
                    value = extractFromNamedRange(sheets, namedRanges.get(tagName));
                } else if (cellTags.containsKey(tagName)) {
                    value = extractCellValue(cellTags.get(tagName));
                }

                value = convertType(value, mapping.getDataType());
                result.put(mapping.getFieldName(), value);
                log.debug("Extracted field: {} tag: {} value: {}", mapping.getFieldName(), tagName, value);
            }
        } catch (Exception e) {
            log.error("Failed to extract data from SpreadJS JSON", e);
            throw new BusinessException("解析电子表格数据失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * Discover all tag names (cell tags + named ranges) in a SpreadJS workbook JSON.
     * Used by TagMappingService to auto-sync tpl_tag_mapping after a template is saved.
     */
    public Set<String> discoverTagNames(String templateJson) {
        Set<String> result = new HashSet<>();
        if (templateJson == null || templateJson.isBlank()) return result;
        try {
            JsonNode root = objectMapper.readTree(templateJson);
            parseNamedRanges(root).keySet().forEach(result::add);
            JsonNode sheets = root.get("sheets");
            if (sheets != null && sheets.isObject()) {
                parseCellTags(sheets).keySet().forEach(result::add);
            }
        } catch (Exception e) {
            log.warn("discoverTagNames: failed to parse templateJson — {}", e.getMessage());
        }
        return result;
    }

    private Map<String, JsonNode> parseNamedRanges(JsonNode root) {
        Map<String, JsonNode> map = new HashMap<>();
        JsonNode names = root.get("names");
        if (names != null && names.isArray()) {
            for (JsonNode nameNode : names) {
                String name = nameNode.has("name") ? nameNode.get("name").asText() : null;
                if (name != null) {
                    map.put(name, nameNode);
                }
            }
        }
        return map;
    }

    private Map<String, JsonNode> parseCellTags(JsonNode sheets) {
        Map<String, JsonNode> map = new HashMap<>();
        var sheetNames = sheets.fieldNames();
        while (sheetNames.hasNext()) {
            String sheetName = sheetNames.next();
            JsonNode dataTable = sheets.get(sheetName).path("data").path("dataTable");
            if (!dataTable.isMissingNode() && dataTable.isObject()) {
                var rows = dataTable.fieldNames();
                while (rows.hasNext()) {
                    String row = rows.next();
                    JsonNode rowNode = dataTable.get(row);
                    var cols = rowNode.fieldNames();
                    while (cols.hasNext()) {
                        String col = cols.next();
                        JsonNode cell = rowNode.get(col);
                        if (cell.has("tag")) {
                            String tag = cell.get("tag").asText();
                            map.put(tag, cell);
                        }
                    }
                }
            }
        }
        return map;
    }

    private Object extractFromNamedRange(JsonNode sheets, JsonNode rangeNode) {
        // TODO: resolve named range to cell value using row/col/sheetIndex from rangeNode
        int sheetIdx = rangeNode.has("sheetIndex") ? rangeNode.get("sheetIndex").asInt() : 0;
        int row = rangeNode.has("row") ? rangeNode.get("row").asInt() : 0;
        int col = rangeNode.has("col") ? rangeNode.get("col").asInt() : 0;

        int idx = 0;
        var it = sheets.fieldNames();
        while (it.hasNext()) {
            String name = it.next();
            if (idx == sheetIdx) {
                return sheets.get(name).path("data").path("dataTable")
                        .path(String.valueOf(row)).path(String.valueOf(col)).path("value").asText(null);
            }
            idx++;
        }
        return null;
    }

    private Object extractCellValue(JsonNode cell) {
        return cell.has("value") ? cell.get("value").asText(null) : null;
    }

    private Object convertType(Object value, String dataType) {
        if (value == null || dataType == null) return value;
        String str = value.toString();
        try {
            return switch (dataType.toUpperCase()) {
                case "NUMBER" -> str.contains(".") ? Double.parseDouble(str) : Long.parseLong(str);
                case "DATE" -> str;
                default -> str;
            };
        } catch (NumberFormatException e) {
            log.warn("Failed to convert value '{}' to type {}", str, dataType);
            return str;
        }
    }
}
