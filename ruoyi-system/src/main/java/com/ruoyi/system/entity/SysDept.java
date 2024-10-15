package com.ruoyi.system.entity;

import com.ruoyi.framework.querydsl.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 部门表
 *
 * @author ruoyi
 */
@Data
public class SysDept extends BaseEntity {

    /**
     * 部门ID
     */
    @Id
    private Long deptId;
    /**
     * 上级部门ID
     */
    private Long parentId;
    /**
     * 祖级列表
     */
    private String ancestor;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 负责人
     */
    private String leader;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态 0正常,1停用
     */
    private String status;

}
