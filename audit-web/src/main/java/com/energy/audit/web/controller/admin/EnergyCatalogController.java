package com.energy.audit.web.controller.admin;

import com.energy.audit.common.result.PageResult;
import com.energy.audit.common.result.R;
import com.energy.audit.model.entity.setting.BsEnergyCatalog;
import com.energy.audit.service.setting.EnergyCatalogService;
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

import java.util.List;

/**
 * Admin energy catalog controller — manages global energy type reference data.
 */
@Tag(name = "Admin Energy Catalog", description = "Global energy catalog CRUD (admin only)")
@RestController
@RequestMapping("/admin/energy-catalog")
public class EnergyCatalogController {

    private final EnergyCatalogService catalogService;

    public EnergyCatalogController(EnergyCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(summary = "Get catalog entry by ID")
    @GetMapping("/{id}")
    public R<BsEnergyCatalog> getById(@PathVariable Long id) {
        return R.ok(catalogService.getById(id));
    }

    @Operation(summary = "List catalog entries with pagination")
    @GetMapping
    public R<PageResult<BsEnergyCatalog>> list(BsEnergyCatalog query,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BsEnergyCatalog> list = catalogService.list(query);
        PageInfo<BsEnergyCatalog> pageInfo = new PageInfo<>(list);
        return R.ok(PageResult.of(pageInfo.getTotal(), pageInfo.getList()));
    }

    @Operation(summary = "Create catalog entry")
    @PostMapping
    public R<Void> create(@RequestBody BsEnergyCatalog catalog) {
        catalogService.create(catalog);
        return R.ok();
    }

    @Operation(summary = "Update catalog entry")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody BsEnergyCatalog catalog) {
        catalog.setId(id);
        catalogService.update(catalog);
        return R.ok();
    }

    @Operation(summary = "Delete catalog entry")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        catalogService.delete(id);
        return R.ok();
    }
}
