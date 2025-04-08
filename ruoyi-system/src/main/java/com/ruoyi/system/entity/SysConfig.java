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
@Table(name = "sys_config")
@Comment(value = "配置表")
@DynamicInsert
@DynamicUpdate
@SoftDelete
public class SysConfig extends AuditableEntity {

    @Id
    @Column(length = 50)
    @Comment(value = "配置主键")
    private String configId;

    @Column(length = 100)
    @Comment(value = "配置名称")
    private String configName;

    @Column(length = 100)
    @Comment(value = "配置键名")
    private String configKey;

    @Column(length = 500)
    @Comment(value = "配置键值")
    private String configValue;

    @Column(length = 1)
    @Comment(value = "系统内置（Y是 N否）")
    private String configType;

    @Column(length = 500)
    @Comment(value = "备注")
    private String remark;

    @Override
    public String getId() {
        return configId;
    }

    @Override
    public void setId(String id) {
        this.configId = id;
    }
}
