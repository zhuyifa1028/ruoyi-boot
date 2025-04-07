package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysConfigDTO;
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
     * 根据配置ID查询详细信息
     */
    SysConfigVO selectConfigById(String configId);

    /**
     * 根据配置键查询配置值
     */
    String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    boolean selectCaptchaEnabled();

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

}
