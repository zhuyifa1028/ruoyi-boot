package com.ruoyi.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel("SysDeptInsertDTO")
@Data
public class SysDeptInsertDTO implements Serializable {

    @ApiModelProperty("上级部门")
    @NotBlank(message = "上级部门不能为空")
    private String parentId;

    @ApiModelProperty("部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    @ApiModelProperty("显示顺序")
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("联系电话")
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @ApiModelProperty("部门状态（0正常；1停用）")
    private String status;

}
