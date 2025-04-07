package com.ruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "SysConfigDTO")
@Data
public class SysConfigDTO implements Serializable {

    @Schema(description = "配置主键")
    private String configId;

    @Schema(description = "配置名称")
    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称不能超过100个字符")
    private String configName;

    @Schema(description = "配置键名")
    @NotBlank(message = "配置键名长度不能为空")
    @Size(max = 100, message = "配置键名长度不能超过100个字符")
    private String configKey;

    @Schema(description = "配置键值")
    @NotBlank(message = "配置键值不能为空")
    @Size(max = 500, message = "配置键值长度不能超过500个字符")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

}
