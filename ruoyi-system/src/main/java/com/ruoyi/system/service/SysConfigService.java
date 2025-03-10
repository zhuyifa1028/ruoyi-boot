package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.vo.SysConfigVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 配置表 业务层
 *
 * @author ruoyi
 */
public interface SysConfigService {

    /**
     * 查询配置列表
     */
    Page<SysConfigVO> selectConfigList(SysConfigQuery query);

    /**
     * 查询配置信息
     */
    SysConfigVO selectConfigById(String configId);

    /**
     * 新增配置
     */
    void insertConfig(SysConfigDTO dto);

    /**
     * 修改配置
     */
    void updateConfig(SysConfigDTO dto);

    /**
     * 批量删除配置
     */
    void deleteConfigByIds(List<String> configIds);

    /**
     * 刷新配置缓存
     */
    void refreshConfigCache();

    /**
     * 查询配置值
     */
    String selectConfigValue(String configKey);

    /**
     * 查询验证码开关
     */
    boolean selectCaptchaEnabled();

}
