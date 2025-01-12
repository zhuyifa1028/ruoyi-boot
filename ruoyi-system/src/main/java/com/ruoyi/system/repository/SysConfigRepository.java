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

    List<SysConfig> selectAllConfig();

    Page<SysConfig> selectByConfigQuery(SysConfigQuery query);

    List<SysConfig> selectByConfigId(Long[] configIds);

    SysConfig selectByConfigId(Long configId);

    SysConfig selectByConfigKey(String configKey);

    void insertConfig(SysConfig config);

    void updateConfig(SysConfig config);

    void deleteConfigById(Long configId);

}
