package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.vo.SysConfigVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 配置管理 业务层
 *
 * @author ruoyi
 */
public interface SysConfigService {

    /**
     * 根据条件分页查询配置列表
     */
    Page<SysConfigVO> selectConfigList(SysConfigQuery query);

    /**
     * 查询配置信息
     */
    SysConfigVO selectConfigById(String configId);

    /**
     * 查询配置值
     */
    String selectConfigValue(String configKey);

    /**
     * 查询验证码开关
     */
    boolean selectCaptchaEnabled();

    /**
     * 新增配置
     */
    void insertConfig(SysConfigInsertDTO insertDTO);

    /**
     * 修改配置
     */
    void updateConfig(SysConfigUpdateDTO updateDTO);

    /**
     * 批量删除配置
     */
    void deleteConfigByIds(List<String> configIds);

    /**
     * 刷新配置缓存
     */
    void refreshConfigCache();

}
