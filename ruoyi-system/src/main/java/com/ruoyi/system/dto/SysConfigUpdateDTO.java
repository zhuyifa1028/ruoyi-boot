package com.ruoyi.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel("SysConfigUpdateDTO")
@Data
public class SysConfigUpdateDTO implements Serializable {

    @ApiModelProperty("配置ID")
    @NotBlank(message = "配置ID不能为空")
    private String configId;

    @ApiModelProperty("配置名称")
    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称不能超过100个字符")
    private String configName;

    @ApiModelProperty("配置键名")
    @NotBlank(message = "配置键名长度不能为空")
    @Size(max = 100, message = "配置键名长度不能超过100个字符")
    private String configKey;

    @ApiModelProperty("配置键值")
    @NotBlank(message = "配置键值不能为空")
    @Size(max = 500, message = "配置键值长度不能超过500个字符")
    private String configValue;

    @ApiModelProperty("系统内置（Y是 N否）")
    private String configType;

    @ApiModelProperty("备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

}
