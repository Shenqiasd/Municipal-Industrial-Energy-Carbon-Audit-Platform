package com.energy.audit.web.controller.template;

import com.energy.audit.common.result.PageResult;
import com.energy.audit.common.result.R;
import com.energy.audit.model.entity.template.TplEditLock;
import com.energy.audit.model.entity.template.TplTemplate;
import com.energy.audit.service.template.EditLockService;
import com.energy.audit.service.template.SpreadsheetDataExtractor;
import com.energy.audit.service.template.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Template management controller
 */
@Tag(name = "Template", description = "Template CRUD, publish, and data extraction operations")
@RestController
@RequestMapping("/template")
public class TemplateController {

    private final TemplateService templateService;
    private final SpreadsheetDataExtractor dataExtractor;
    private final EditLockService editLockService;

    public TemplateController(TemplateService templateService,
                              SpreadsheetDataExtractor dataExtractor,
                              EditLockService editLockService) {
        this.templateService = templateService;
        this.dataExtractor = dataExtractor;
        this.editLockService = editLockService;
    }

    @Operation(summary = "Get template by ID")
    @GetMapping("/{id}")
    public R<TplTemplate> getById(@PathVariable Long id) {
        return R.ok(templateService.getById(id));
    }

    @Operation(summary = "Get template by code")
    @GetMapping("/code/{code}")
    public R<TplTemplate> getByCode(@PathVariable String code) {
        return R.ok(templateService.getByCode(code));
    }

    @Operation(summary = "List templates with pagination")
    @GetMapping
    public R<PageResult<TplTemplate>> list(TplTemplate query,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TplTemplate> list = templateService.list(query);
        PageInfo<TplTemplate> pageInfo = new PageInfo<>(list);
        return R.ok(PageResult.of(pageInfo.getTotal(), pageInfo.getList()));
    }

    @Operation(summary = "Create template")
    @PostMapping
    public R<Void> create(@RequestBody TplTemplate template) {
        templateService.create(template);
        return R.ok();
    }

    @Operation(summary = "Update template")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody TplTemplate template) {
        template.setId(id);
        templateService.update(template);
        return R.ok();
    }

    @Operation(summary = "Delete template")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return R.ok();
    }

    @Operation(summary = "Publish template")
    @PostMapping("/{id}/publish")
    public R<Void> publish(@PathVariable Long id) {
        templateService.publish(id);
        return R.ok();
    }

    @Operation(summary = "Extract data from SpreadJS JSON")
    @PostMapping("/extract-data")
    public R<Map<String, Object>> extractData(@RequestBody Map<String, String> request) {
        String spreadjsJson = request.get("spreadjsJson");
        // TODO: load tag mappings from template version
        Map<String, Object> data = dataExtractor.extractData(spreadjsJson, Collections.emptyList());
        return R.ok(data);
    }

    @Operation(summary = "Acquire edit lock")
    @PostMapping("/lock")
    public R<TplEditLock> acquireLock(@RequestParam Long enterpriseId,
                                      @RequestParam Long templateId,
                                      @RequestParam Integer auditYear) {
        return R.ok(editLockService.acquireLock(enterpriseId, templateId, auditYear));
    }

    @Operation(summary = "Release edit lock")
    @DeleteMapping("/lock")
    public R<Void> releaseLock(@RequestParam Long enterpriseId,
                               @RequestParam Long templateId,
                               @RequestParam Integer auditYear) {
        editLockService.releaseLock(enterpriseId, templateId, auditYear);
        return R.ok();
    }

    @Operation(summary = "Check edit lock status")
    @GetMapping("/lock")
    public R<TplEditLock> checkLock(@RequestParam Long enterpriseId,
                                    @RequestParam Long templateId,
                                    @RequestParam Integer auditYear) {
        return R.ok(editLockService.checkLock(enterpriseId, templateId, auditYear));
    }
}
