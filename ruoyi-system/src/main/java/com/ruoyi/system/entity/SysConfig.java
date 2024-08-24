package com.ruoyi.system.entity;

import com.ruoyi.framework.jpa.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Table(appliesTo = "sys_config", comment = "系统配置表")
@Entity
@Data
public class SysConfig extends BaseEntity {

    @Comment(value = "配置ID")
    @Column(length = 20)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @Comment(value = "配置名称")
    @Column(length = 100)
    private String configName;

    @Comment(value = "配置键")
    @Column(length = 100)
    private String configKey;

    @Comment(value = "配置值")
    @Column(length = 500)
    private String configValue;

    @Comment(value = "系统内置（Y是 N否）")
    @Column(length = 1)
    private String configType;

    @Comment(value = "备注")
    @Column(length = 500)
    private String remark;

}
