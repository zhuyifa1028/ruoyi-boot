package com.ruoyi.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 配置表 sys_config
 *
 * @author ruoyi
 */
@Data
public class SysConfig implements Serializable {

    /**
     * 配置编号
     */
    private Long configId;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    private String remark;

}
