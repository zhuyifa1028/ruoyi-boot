package com.ruoyi.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Schema(description = "系统配置表 Insert DTO")
@Data
public class SysConfigInsertDTO implements Serializable {

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

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

}
