package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {

    SysConfig selectByConfigKey(String configKey);

}
