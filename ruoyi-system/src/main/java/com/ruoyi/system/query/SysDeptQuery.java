package com.ruoyi.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "部门表 Query")
@Data
public class SysDeptQuery implements Serializable {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态 0正常,1停用")
    private String status;

}
