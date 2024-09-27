package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Resource
    private SysConfigConverter sysConfigConverter;
    @Resource
    private SysConfigRepository sysConfigRepository;

    @Resource
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化配置到缓存
     */
    @PostConstruct
    public void init() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 根据条件分页查询配置列表
     */
    @Override
    public Page<SysConfigVO> selectConfigList(SysConfigQuery query) {
        Page<SysConfig> page = sysConfigRepository.selectConfigList(query);
        return page.map(item -> sysConfigConverter.toSysConfigVO(item));
    }

    /**
     * 根据配置ID查询详细信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfigVO selectConfigById(Long configId) {
        SysConfig config = sysConfigRepository.selectByConfigId(configId);
        return sysConfigConverter.toSysConfigVO(config);
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigInsertDTO dto) {
        SysConfig config = sysConfigRepository.selectByConfigKey(dto.getConfigKey());
        if (ObjectUtils.isNotNull(config)) {
            throw new ServiceException("新增配置'" + dto.getConfigName() + "'失败，配置键名已存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);

        sysConfigRepository.insert(entity);

        redisCache.setCacheObject(getCacheKey(dto.getConfigKey()), dto.getConfigValue());
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigUpdateDTO dto) {
        SysConfig config = sysConfigRepository.selectByConfigId(dto.getConfigId());
        if (ObjectUtils.isNull(config)) {
            throw new ServiceException("修改配置'" + dto.getConfigName() + "'失败，配置不存在");
        }
        if (ObjectUtils.notEqual(config.getConfigKey(), dto.getConfigKey())) {
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }

        SysConfig check = sysConfigRepository.selectByConfigKey(dto.getConfigKey());
        if (ObjectUtils.isNotNull(check) && ObjectUtils.notEqual(check.getConfigId(), dto.getConfigId())) {
            throw new ServiceException("修改配置'" + dto.getConfigName() + "'失败，配置键名已存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);

        sysConfigRepository.update(entity);

        redisCache.setCacheObject(getCacheKey(dto.getConfigKey()), dto.getConfigValue());
    }

    /**
     * 批量删除配置
     */
    @Override
    public void deleteConfigByIds(List<Long> configIds) {
        List<SysConfig> configList = sysConfigRepository.selectByConfigId(configIds);

        configList.forEach(config -> {
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("系统配置【%1$s】不能删除", config.getConfigKey()));
            }
            sysConfigRepository.delete(config);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        });
    }

    /**
     * 刷新配置缓存
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 加载参数缓存数据
     */
    private void loadingConfigCache() {
        List<SysConfig> configsList = sysConfigRepository.selectConfigList();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    private void clearConfigCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 根据配置键名查询参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }

        SysConfig retConfig = sysConfigRepository.selectByConfigKey(configKey);
        if (ObjectUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }
}
