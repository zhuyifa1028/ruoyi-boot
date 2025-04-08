package com.ruoyi.system.entity;

import com.ruoyi.framework.jpa.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SoftDelete;

@Data
@Entity
@Table(name = "sys_dept")
@Comment(value = "部门表")
@DynamicInsert
@DynamicUpdate
@SoftDelete
public class SysDept extends AuditableEntity {

    @Id
    @Column(length = 50)
    @Comment(value = "部门ID")
    private String deptId;

    @Column(length = 50)
    @Comment(value = "父部门ID")
    private String parentId;

    @Column(length = 50)
    @Comment(value = "祖级列表")
    private String ancestors;

    @Column(length = 50)
    @Comment(value = "部门名称")
    private String deptName;

    @Column(length = 20)
    @Comment(value = "负责人")
    private String leader;

    @Column(length = 11)
    @Comment(value = "联系电话")
    private String phone;

    @Column(length = 50)
    @Comment(value = "邮箱")
    private String email;

    @Column(length = 11)
    @Comment(value = "显示顺序")
    private Integer orderNum;

    @Column(length = 1)
    @Comment(value = "部门状态（0正常 1停用）")
    private Character status;

    @Override
    public void setId(String id) {
        this.deptId = id;
    }

    @Override
    public String getId() {
        return deptId;
    }
}
