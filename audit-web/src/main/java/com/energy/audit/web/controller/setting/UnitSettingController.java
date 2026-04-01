package com.energy.audit.web.controller.setting;

import com.energy.audit.common.result.R;
import com.energy.audit.model.entity.setting.BsUnit;
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
 * Unit setting controller
 */
@Tag(name = "Unit Settings", description = "Unit CRUD operations")
@RestController
@RequestMapping("/setting/unit")
public class UnitSettingController {

    @Operation(summary = "Get unit by ID")
    @GetMapping("/{id}")
    public R<BsUnit> getById(@PathVariable Long id) {
        // TODO: inject and use UnitSettingService
        return R.ok();
    }

    @Operation(summary = "List units with pagination")
    @GetMapping
    public R<List<BsUnit>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        // TODO: inject and use UnitSettingService
        return R.ok();
    }

    @Operation(summary = "Create unit")
    @PostMapping
    public R<Void> create(@RequestBody BsUnit unit) {
        // TODO: inject and use UnitSettingService
        return R.ok();
    }

    @Operation(summary = "Update unit")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody BsUnit unit) {
        // TODO: inject and use UnitSettingService
        return R.ok();
    }

    @Operation(summary = "Delete unit")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        // TODO: inject and use UnitSettingService
        return R.ok();
    }
}
