package com.ruoyi.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("SysConfigVO")
@Data
public class SysConfigVO implements Serializable {

    @ApiModelProperty("配置ID")
    private String configId;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置键名")
    private String configKey;

    @ApiModelProperty("配置键值")
    private String configValue;

    @ApiModelProperty("系统内置（Y是 N否）")
    private String configType;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
