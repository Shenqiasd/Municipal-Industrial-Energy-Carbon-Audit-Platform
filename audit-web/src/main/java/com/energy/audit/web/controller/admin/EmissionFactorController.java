package com.energy.audit.web.controller.admin;

import com.energy.audit.common.result.PageResult;
import com.energy.audit.common.result.R;
import com.energy.audit.model.entity.config.CmEmissionFactor;
import com.energy.audit.service.config.EmissionFactorService;
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
 * Admin emission factor controller — manages global carbon emission factors.
 */
@Tag(name = "Admin Emission Factors", description = "Carbon emission factor CRUD (admin only)")
@RestController
@RequestMapping("/admin/emission-factor")
public class EmissionFactorController {

    private final EmissionFactorService factorService;

    public EmissionFactorController(EmissionFactorService factorService) {
        this.factorService = factorService;
    }

    @Operation(summary = "Get emission factor by ID")
    @GetMapping("/{id}")
    public R<CmEmissionFactor> getById(@PathVariable Long id) {
        return R.ok(factorService.getById(id));
    }

    @Operation(summary = "List emission factors with pagination")
    @GetMapping
    public R<PageResult<CmEmissionFactor>> list(CmEmissionFactor query,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CmEmissionFactor> list = factorService.list(query);
        PageInfo<CmEmissionFactor> pageInfo = new PageInfo<>(list);
        return R.ok(PageResult.of(pageInfo.getTotal(), pageInfo.getList()));
    }

    @Operation(summary = "Create emission factor")
    @PostMapping
    public R<Void> create(@RequestBody CmEmissionFactor factor) {
        factorService.create(factor);
        return R.ok();
    }

    @Operation(summary = "Update emission factor")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody CmEmissionFactor factor) {
        factor.setId(id);
        factorService.update(factor);
        return R.ok();
    }

    @Operation(summary = "Delete emission factor")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        factorService.delete(id);
        return R.ok();
    }
}
