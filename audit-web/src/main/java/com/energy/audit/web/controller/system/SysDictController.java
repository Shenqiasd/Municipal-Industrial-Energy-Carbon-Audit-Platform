package com.energy.audit.web.controller.system;

import com.energy.audit.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * System dictionary controller
 */
@Tag(name = "System Dictionaries", description = "Dictionary CRUD operations")
@RestController
@RequestMapping("/system/dicts")
public class SysDictController {

    @Operation(summary = "Get dictionary by ID")
    @GetMapping("/{id}")
    public R<Map<String, Object>> getById(@PathVariable Long id) {
        // TODO: implement dictionary service
        return R.ok();
    }

    @Operation(summary = "List dictionaries")
    @GetMapping
    public R<List<Map<String, Object>>> list() {
        // TODO: implement dictionary service
        return R.ok();
    }

    @Operation(summary = "Get dictionary items by type")
    @GetMapping("/type/{dictType}")
    public R<List<Map<String, Object>>> getByType(@PathVariable String dictType) {
        // TODO: implement dictionary service
        return R.ok();
    }

    @Operation(summary = "Create dictionary")
    @PostMapping
    public R<Void> create(@RequestBody Map<String, Object> dict) {
        // TODO: implement dictionary service
        return R.ok();
    }

    @Operation(summary = "Update dictionary")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Map<String, Object> dict) {
        // TODO: implement dictionary service
        return R.ok();
    }

    @Operation(summary = "Delete dictionary")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        // TODO: implement dictionary service
        return R.ok();
    }
}
