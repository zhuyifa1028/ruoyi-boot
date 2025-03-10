package com.ruoyi.system.query;

import com.ruoyi.common.core.page.PageDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "配置表 Query")
@Data
public class SysConfigQuery extends PageDomain {

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置键名")
    private String configKey;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "备注")
    private LocalDate startDate;

    @Schema(description = "备注")
    private LocalDate endDate;

}
