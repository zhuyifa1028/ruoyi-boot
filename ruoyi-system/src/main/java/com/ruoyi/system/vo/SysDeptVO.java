package com.ruoyi.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("SysDeptVO")
@Data
public class SysDeptVO implements Serializable {

    @ApiModelProperty("部门ID")
    private String deptId;

    @ApiModelProperty("父部门ID")
    private String parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("部门状态（0正常；1停用）")
    private String status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
