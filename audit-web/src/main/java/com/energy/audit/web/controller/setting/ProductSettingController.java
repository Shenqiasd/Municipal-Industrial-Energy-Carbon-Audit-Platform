package com.energy.audit.web.controller.setting;

import com.energy.audit.common.result.R;
import com.energy.audit.model.entity.setting.BsProduct;
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
 * Product setting controller
 */
@Tag(name = "Product Settings", description = "Product CRUD operations")
@RestController
@RequestMapping("/setting/product")
public class ProductSettingController {

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public R<BsProduct> getById(@PathVariable Long id) {
        // TODO: inject and use ProductSettingService
        return R.ok();
    }

    @Operation(summary = "List products with pagination")
    @GetMapping
    public R<List<BsProduct>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        // TODO: inject and use ProductSettingService
        return R.ok();
    }

    @Operation(summary = "Create product")
    @PostMapping
    public R<Void> create(@RequestBody BsProduct product) {
        // TODO: inject and use ProductSettingService
        return R.ok();
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody BsProduct product) {
        // TODO: inject and use ProductSettingService
        return R.ok();
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        // TODO: inject and use ProductSettingService
        return R.ok();
    }
}
