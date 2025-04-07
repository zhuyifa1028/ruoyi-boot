package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface SysConfigRepository extends BaseRepository<SysConfig> {

    SysConfig findByConfigKey(String configKey);

}
