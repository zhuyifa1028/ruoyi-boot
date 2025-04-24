package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("【系统管理】部门管理")
@Validated
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    @ApiOperation("根据条件查询部门列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public R<List<SysDeptVO>> selectDeptList(@Valid SysDeptQuery query) {
        return R.ok(sysDeptService.selectDeptList(query));
    }

    @ApiOperation("查询部门列表（排除节点）")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public R<List<SysDeptVO>> selectDeptExcludeList(@PathVariable(required = false) String deptId) {
        return R.ok(sysDeptService.selectDeptExcludeList(deptId));
    }

    @ApiOperation("查询部门信息")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public R<SysDeptVO> selectDeptById(@PathVariable String deptId) {
        sysDeptService.checkDeptDataScope(deptId);
        return R.ok(sysDeptService.selectDeptById(deptId));
    }

    @ApiOperation("新增部门")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @PostMapping
    public R<Void> insertDept(@RequestBody @Valid SysDeptInsertDTO dto) {
        sysDeptService.insertDept(dto);
        return R.ok();
    }

    @ApiOperation("修改部门")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @PutMapping
    public R<Void> updateDept(@RequestBody @Valid SysDeptUpdateDTO dto) {
        sysDeptService.checkDeptDataScope(dto.getDeptId());
        sysDeptService.updateDept(dto);
        return R.ok();
    }

    @ApiOperation("删除部门")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @DeleteMapping("/{deptId}")
    public R<Void> remove(@PathVariable String deptId) {
        sysDeptService.checkDeptDataScope(deptId);
        sysDeptService.deleteDeptById(deptId);
        return R.ok();
    }

}
