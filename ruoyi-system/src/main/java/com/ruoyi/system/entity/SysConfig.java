package com.ruoyi.system.entity;

import com.ruoyi.framework.querydsl.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 系统配置表
 */
@Data
public class SysConfig extends BaseEntity {

    /**
     * 配置ID
     */
    @Id
    private Long configId;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置键名
     */
    private String configKey;
    /**
     * 配置键值
     */
    private String configValue;
    /**
     * 系统内置（Y是 N否）
     */
    private String configType;
    /**
     * 备注
     */
    private String remark;

}
