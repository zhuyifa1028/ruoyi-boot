package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "配置表 接口层")
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @Operation(summary = "查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/selectConfigList")
    public TableDataInfo selectConfigList(SysConfigQuery query) {
        return getDataTable(sysConfigService.selectConfigList(query));
    }

    @Operation(summary = "查询配置信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/selectConfig/{configId}")
    public AjaxResult selectConfig(@PathVariable Long configId) {
        return success(sysConfigService.selectConfigById(configId));
    }

    @Operation(summary = "查询配置值")
    @GetMapping("/selectConfigValue/{configKey}")
    public AjaxResult selectConfigValue(@PathVariable String configKey) {
        return success(sysConfigService.selectConfigValue(configKey));
    }

    @Operation(summary = "新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping("/insertConfig")
    public AjaxResult insertConfig(@Validated @RequestBody SysConfigDTO dto) {
        sysConfigService.insertConfig(dto);
        return success();
    }

    @Operation(summary = "修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping("/updateConfig")
    public AjaxResult updateConfig(@Validated @RequestBody SysConfigDTO dto) {
        sysConfigService.updateConfig(dto);
        return success();
    }

    @Operation(summary = "删除配置")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/deleteConfig/{configIds}")
    public AjaxResult deleteConfig(@PathVariable Long[] configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return success();
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshConfigCache")
    public AjaxResult refreshConfigCache() {
        sysConfigService.refreshConfigCache();
        return success();
    }

}
