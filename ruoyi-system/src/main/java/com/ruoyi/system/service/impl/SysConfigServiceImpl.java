package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 配置表 业务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Resource
    private SysConfigRepository sysConfigRepository;
    @Resource
    private SysConfigConverter sysConfigConverter;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询配置列表
     */
    @Override
    public Page<SysConfigVO> selectConfigList(SysConfigQuery query) {
        Page<SysConfig> page = sysConfigRepository.selectByConfigQuery(query);
        return page.map(item -> sysConfigConverter.toSysConfigVO(item));
    }

    /**
     * 查询配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfigVO selectConfigById(Long configId) {
        SysConfig config = sysConfigRepository.selectByConfigId(configId);
        return sysConfigConverter.toSysConfigVO(config);
    }

    /**
     * 查询配置值
     */
    @Override
    public String selectConfigValue(String configKey) {
        String configValue = redisCache.getCacheObject(getCacheKey(configKey));
        if (StringUtils.isNotBlank(configValue)) {
            return configValue;
        }

        SysConfig config = sysConfigRepository.selectByConfigKey(configKey);
        if (ObjectUtils.isNotEmpty(config)) {

            redisCache.setCacheObject(getCacheKey(configKey), config.getConfigValue());
            return config.getConfigValue();
        }

        return StringUtils.EMPTY;
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigDTO dto) {
        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        checkConfigKeyUnique(entity);
        sysConfigRepository.insertConfig(entity);

        redisCache.setCacheObject(getCacheKey(entity.getConfigKey()), entity.getConfigValue());
    }

    /**
     * 校验配置键是否唯一
     */
    private void checkConfigKeyUnique(SysConfig entity) {
        SysConfig info = sysConfigRepository.selectByConfigKey(entity.getConfigKey());
        if (ObjectUtils.isNotEmpty(info) && ObjectUtils.notEqual(info.getConfigId(), entity.getConfigId())) {
            throw new BaseException("配置键'" + entity.getConfigKey() + "'已存在");
        }
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigDTO dto) {
        SysConfig config = sysConfigRepository.selectByConfigId(dto.getConfigId());
        if (ObjectUtils.isEmpty(config)) {
            throw new BaseException("配置信息不存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        checkConfigKeyUnique(entity);
        sysConfigRepository.updateConfig(entity);

        if (ObjectUtils.notEqual(entity.getConfigKey(), config.getConfigKey())) {
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
        redisCache.setCacheObject(getCacheKey(entity.getConfigKey()), entity.getConfigValue());
    }

    /**
     * 删除配置
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        List<SysConfig> configList = sysConfigRepository.selectByConfigId(configIds);
        for (SysConfig config : configList) {
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置配置【%1$s】不能删除 ", config.getConfigName()));
            }
            sysConfigRepository.deleteConfigById(config.getConfigId());
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 刷新配置缓存
     */
    @PostConstruct
    @Override
    public void refreshConfigCache() {
        // 清空缓存数据
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);

        // 加载缓存数据
        List<SysConfig> configsList = sysConfigRepository.selectAllConfig();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 设置cache key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigValue("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

}
