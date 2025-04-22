package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 配置表 数据层
 *
 * @author ruoyi
 */
public interface SysConfigRepository {

    Page<SysConfig> selectConfigList(SysConfigQuery query);

    List<SysConfig> selectConfigList();

    List<SysConfig> selectByConfigId(List<String> configIds);

    SysConfig selectByConfigId(String configId);

    SysConfig selectByConfigKey(String configKey);

    void insertConfig(SysConfig entity);

    void updateConfig(SysConfig entity);

    void deleteByConfigId(List<String> configIds);

}
