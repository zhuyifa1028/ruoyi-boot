package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.vo.SysConfigVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 参数配置 服务层
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
    SysConfigVO selectConfigById(Long configId);

    /**
     * 新增配置
     */
    void insertConfig(SysConfigInsertDTO dto);

    /**
     * 修改配置
     */
    void updateConfig(SysConfigUpdateDTO dto);

    /**
     * 批量删除配置
     */
    void deleteConfigByIds(List<Long> configIds);

    /**
     * 刷新配置缓存
     */
    void resetConfigCache();

    /**
     * 根据配置键名查询参数键值
     */
    String selectConfigByKey(String configKey);

    /**
     * 获取验证码开关
     */
    boolean selectCaptchaEnabled();

}
