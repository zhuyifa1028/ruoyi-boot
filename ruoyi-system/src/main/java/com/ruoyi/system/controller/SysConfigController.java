package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "配置管理")
@Validated
@RestController
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @Operation(summary = "查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/system/config/list")
    public TableDataInfo list(@Valid SysConfigQuery query) {
        Page<SysConfigVO> page = sysConfigService.selectConfigList(query);
        return getDataTable(page);
    }

    @Operation(summary = "查询配置信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/system/config/{configId}")
    public AjaxResult info(@PathVariable String configId) {
        SysConfigVO configVO = sysConfigService.selectConfigById(configId);
        return success(configVO);
    }

    @Operation(summary = "新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping("/system/config")
    public AjaxResult add(@RequestBody @Valid SysConfigDTO dto) {
        sysConfigService.insertConfig(dto);
        return success();
    }

    @Operation(summary = "修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping("/system/config")
    public AjaxResult edit(@RequestBody @Valid SysConfigDTO dto) {
        sysConfigService.updateConfig(dto);
        return success();
    }

    @Operation(summary = "批量删除配置")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/system/config/{configIds}")
    public AjaxResult remove(@PathVariable List<String> configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return success();
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/system/config/refreshCache")
    public AjaxResult refreshCache() {
        sysConfigService.refreshConfigCache();
        return success();
    }

    @Operation(summary = "查询配置值")
    @GetMapping("/system/config/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        String configValue = sysConfigService.selectConfigValue(configKey);
        return success(configValue);
    }

}
