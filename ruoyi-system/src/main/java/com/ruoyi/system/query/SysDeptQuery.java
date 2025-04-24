package com.ruoyi.system.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("SysDeptQuery")
@Data
public class SysDeptQuery implements Serializable {

    @ApiModelProperty("参数名称")
    private String deptId;

    @ApiModelProperty("系统内置（Y是 N否）")
    private String parentId;

    @ApiModelProperty("参数键名")
    private String deptName;

    @ApiModelProperty("参数键名")
    private String status;

}
