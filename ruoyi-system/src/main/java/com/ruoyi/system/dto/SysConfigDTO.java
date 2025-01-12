package com.ruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(description = "配置表 DTO")
@Data
public class SysConfigDTO implements Serializable {

    @Schema(description = "配置编号")
    private Long configId;

    @Schema(description = "配置名称")
    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称不能超过100个字符")
    private String configName;

    @Schema(description = "配置键")
    @NotBlank(message = "配置键长度不能为空")
    @Size(max = 100, message = "配置键长度不能超过100个字符")
    private String configKey;

    @Schema(description = "配置值")
    @NotBlank(message = "配置值不能为空")
    @Size(max = 500, message = "配置值长度不能超过500个字符")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "备注")
    private String remark;

}
