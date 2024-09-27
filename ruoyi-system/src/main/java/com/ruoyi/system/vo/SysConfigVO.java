package com.ruoyi.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "系统配置表 VO")
@Data
public class SysConfigVO implements Serializable {

    @Schema(description = "配置ID")
    private Long configId;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "备注")
    private String remark;

}
