package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "系统配置表")
@RestController
@RequestMapping(value = "/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService configService;

    @Operation(summary = "根据条件分页查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping(value = "/list")
    public TableDataInfo list(@ParameterObject SysConfigQuery query) {
        startPage();
        Page<SysConfigVO> page = configService.selectConfigList(query);
        return getDataTable(page);
    }

    @Operation(summary = "根据配置ID查询详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId) {
        return success(configService.selectConfigById(configId));
    }

    @Operation(summary = "新增配置")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SysConfigInsertDTO dto) {
        configService.insertConfig(dto);
        return success();
    }

    @Operation(summary = "修改配置")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated SysConfigUpdateDTO dto) {
        configService.updateConfig(dto);
        return success();
    }

    @Operation(summary = "批量删除配置")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping(value = "/{configIds}")
    public AjaxResult remove(@PathVariable List<Long> configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    @Operation(summary = "刷新配置缓存")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping(value = "/refreshCache")
    public AjaxResult refreshCache() {
        configService.resetConfigCache();
        return success();
    }

    @Operation(summary = "根据配置键名查询参数键值")
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return success(configService.selectConfigByKey(configKey));
    }
}
