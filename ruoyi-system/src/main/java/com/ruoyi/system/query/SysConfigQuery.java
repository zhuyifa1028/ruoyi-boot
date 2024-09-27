package com.ruoyi.system.query;

import com.ruoyi.framework.web.domain.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "系统配置表 Query")
@Data
public class SysConfigQuery extends PageQuery {

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "创建时间 start")
    private LocalDate startTime;

    @Schema(description = "创建时间 end")
    private LocalDate endTime;

}
