package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.AjaxResult;
import com.ruoyi.framework.web.PageResult;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "配置管理")
@Validated
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @Operation(summary = "根据条件分页查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public PageResult<List<SysConfigVO>> selectConfigList(@Valid SysConfigQuery query) {
        return PageResult.ok(sysConfigService.selectConfigList(query));
    }

    @Operation(summary = "根据配置ID查询详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/{configId}")
    public AjaxResult<SysConfigVO> selectConfigById(@PathVariable String configId) {
        return AjaxResult.ok(sysConfigService.selectConfigById(configId));
    }

    @Operation(summary = "根据配置键查询配置值")
    @GetMapping("/configKey/{configKey}")
    public AjaxResult<String> selectConfigByKey(@PathVariable String configKey) {
        return AjaxResult.ok(sysConfigService.selectConfigByKey(configKey));
    }

    @Operation(summary = "新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping
    public AjaxResult<Void> insertConfig(@RequestBody @Valid SysConfigDTO dto) {
        sysConfigService.insertConfig(dto);
        return AjaxResult.ok();
    }

    @Operation(summary = "修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public AjaxResult<Void> updateConfig(@RequestBody @Valid SysConfigDTO dto) {
        sysConfigService.updateConfig(dto);
        return AjaxResult.ok();
    }

    @Operation(summary = "批量删除配置")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/{configIds}")
    public AjaxResult<Void> deleteConfigByIds(@PathVariable List<String> configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return AjaxResult.ok();
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public AjaxResult<Void> refreshConfigCache() {
        sysConfigService.refreshConfigCache();
        return AjaxResult.ok();
    }

}
