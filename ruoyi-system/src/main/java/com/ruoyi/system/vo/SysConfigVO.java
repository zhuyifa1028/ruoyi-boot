package com.ruoyi.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "SysConfigVO")
@Data
public class SysConfigVO implements Serializable {

    @Schema(description = "配置主键")
    private String configId;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置键名")
    private String configKey;

    @Schema(description = "配置键值")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    private String remark;

}
