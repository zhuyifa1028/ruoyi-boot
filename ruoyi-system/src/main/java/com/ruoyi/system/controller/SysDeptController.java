package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.AjaxResult;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门管理")
@Validated
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Resource
    private SysDeptService deptService;

    @Operation(summary = "根据条件查询部门列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult<List<SysDeptVO>> selectDeptList(@Valid SysDeptQuery query) {
        return AjaxResult.ok(deptService.selectDeptList(query));
    }

    @Operation(summary = "查询部门列表（排除节点）")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult<List<SysDeptVO>> selectExcludeList(@PathVariable(required = false) String deptId) {
        return AjaxResult.ok(deptService.selectExcludeList(deptId));
    }

    @Operation(summary = "根据部门ID查询详细信息")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult<SysDeptVO> selectDeptById(@PathVariable String deptId) {
        return AjaxResult.ok(deptService.selectDeptById(deptId));
    }

    @Operation(summary = "新增部门")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @PostMapping
    public AjaxResult<Void> insertDept(@RequestBody @Valid SysDeptDTO dto) {
        deptService.insertDept(dto);
        return AjaxResult.ok();
    }

    @Operation(summary = "修改部门")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @PutMapping
    public AjaxResult<Void> updateDept(@RequestBody @Valid SysDeptDTO dto) {
        deptService.updateDept(dto);
        return AjaxResult.ok();
    }

    @Operation(summary = "删除部门")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @DeleteMapping("/{deptId}")
    public AjaxResult<Void> deleteDeptById(@PathVariable String deptId) {
        deptService.deleteDeptById(deptId);
        return AjaxResult.ok();
    }

}
