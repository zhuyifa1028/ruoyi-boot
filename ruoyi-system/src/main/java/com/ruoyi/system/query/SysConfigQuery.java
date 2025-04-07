package com.ruoyi.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "SysConfigQuery")
@Data
public class SysConfigQuery implements Serializable {

    @Schema(description = "页码")
    @NotNull(message = "页码不能为空")
    private Integer pageNumber;

    @Schema(description = "页面大小")
    @NotNull(message = "页面大小不能为空")
    private Integer pageSize;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "创建时间 start")
    private LocalDate beginTime;

    @Schema(description = "创建时间 end")
    private LocalDate endTime;

}
