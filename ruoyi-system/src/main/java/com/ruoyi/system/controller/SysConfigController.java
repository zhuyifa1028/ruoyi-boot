package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api("【系统管理】配置管理")
@Validated
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;

    @ApiOperation("根据条件分页查询配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public R<List<SysConfigVO>> selectConfigList(@Valid SysConfigQuery query) {
        return R.ok(sysConfigService.selectConfigList(query));
    }

    @ApiOperation("查询配置信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public R<SysConfigVO> selectConfigById(@PathVariable String configId) {
        return R.ok(sysConfigService.selectConfigById(configId));
    }

    @ApiOperation("查询配置值")
    @GetMapping(value = "/configKey/{configKey}")
    public R<String> selectConfigValue(@PathVariable String configKey) {
        return R.ok(sysConfigService.selectConfigValue(configKey));
    }

    @ApiOperation("新增配置")
    @Log(title = "配置管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping
    public R<Void> insertConfig(@RequestBody @Valid SysConfigInsertDTO insertDTO) {
        sysConfigService.insertConfig(insertDTO);
        return R.ok();
    }

    @ApiOperation("修改配置")
    @Log(title = "配置管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public R<Void> updateConfig(@RequestBody @Valid SysConfigUpdateDTO updateDTO) {
        sysConfigService.updateConfig(updateDTO);
        return R.ok();
    }

    @ApiOperation("批量删除配置")
    @Log(title = "配置管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/{configIds}")
    public R<Void> deleteConfigByIds(@PathVariable List<String> configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return R.ok();
    }

    @ApiOperation("刷新配置缓存")
    @Log(title = "配置管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public R<Void> refreshConfigCache() {
        sysConfigService.refreshConfigCache();
        return R.ok();
    }

}
