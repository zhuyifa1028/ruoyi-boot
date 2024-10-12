package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SysConfigRepository {

    void insert(SysConfig entity);

    void update(SysConfig entity);

    void delete(SysConfig entity);

    SysConfig selectByConfigId(Long configId);

    SysConfig selectByConfigKey(String configKey);

    List<SysConfig> selectByConfigId(List<Long> configIds);

    List<SysConfig> selectConfigList();

    Page<SysConfig> selectConfigList(SysConfigQuery query);
}
