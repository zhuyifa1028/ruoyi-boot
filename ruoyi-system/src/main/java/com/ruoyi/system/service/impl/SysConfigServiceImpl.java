package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 配置管理 业务层实现
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
     * 根据条件分页查询配置列表
     */
    @Override
    public Page<SysConfigVO> selectConfigList(SysConfigQuery query) {
        Page<SysConfig> page = sysConfigRepository.selectConfigList(query);

        return page.map(sysConfigConverter::toSysConfigVO);
    }

    /**
     * 查询配置信息
     */
    @Override
    public SysConfigVO selectConfigById(String configId) {
        SysConfig info = sysConfigRepository.selectByConfigId(configId);
        if (Objects.isNull(info)) {
            throw new ServiceException("配置信息不存在，请检查ID");
        }

        return sysConfigConverter.toSysConfigVO(info);
    }

    /**
     * 查询配置值
     */
    @Override
    public String selectConfigValue(String configKey) {
        String configValue = redisCache.getCacheObject(CacheConstants.SYS_CONFIG_KEY + configKey);
        if (Objects.nonNull(configValue)) {
            return configValue;
        }

        SysConfig info = sysConfigRepository.selectByConfigKey(configKey);
        if (Objects.nonNull(info)) {

            redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + configKey, info.getConfigValue());
            return info.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询验证码开关
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigValue("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return false;
        }
        return BooleanUtils.toBoolean(captchaEnabled);
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigInsertDTO insertDTO) {
        SysConfig check = sysConfigRepository.selectByConfigKey(insertDTO.getConfigKey());
        if (Objects.nonNull(check)) {
            throw new ServiceException(String.format("新增参数'%s'失败，参数键名已存在", insertDTO.getConfigName()));
        }

        SysConfig entity = sysConfigConverter.toSysConfig(insertDTO);
        entity.setConfigId(IdUtils.fastUUID());
        entity.setCreateBy(SecurityUtils.getUsername());
        entity.setCreateTime(LocalDateTime.now());
        sysConfigRepository.insertConfig(entity);

        redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + insertDTO.getConfigKey(), insertDTO.getConfigValue());
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigUpdateDTO updateDTO) {
        SysConfig info = sysConfigRepository.selectByConfigId(updateDTO.getConfigId());
        if (Objects.isNull(info)) {
            throw new ServiceException("配置信息不存在，请检查ID");
        }
        SysConfig check = sysConfigRepository.selectByConfigKey(updateDTO.getConfigKey());
        if (Objects.nonNull(check) && ObjectUtils.notEqual(check.getConfigId(), updateDTO.getConfigId())) {
            throw new ServiceException(String.format("修改参数'%s'失败，参数键名已存在", updateDTO.getConfigName()));
        }

        SysConfig entity = sysConfigConverter.toSysConfig(updateDTO);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());
        sysConfigRepository.updateConfig(entity);

        if (!StringUtils.equals(info.getConfigKey(), updateDTO.getConfigKey())) {
            redisCache.deleteObject(CacheConstants.SYS_CONFIG_KEY + info.getConfigKey());
        }
        redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + updateDTO.getConfigKey(), updateDTO.getConfigValue());
    }

    /**
     * 批量删除配置
     */
    @Override
    public void deleteConfigByIds(List<String> configIds) {
        List<SysConfig> configList = sysConfigRepository.selectByConfigId(configIds);

        List<String> cacheKeys = Lists.newArrayList();
        for (SysConfig config : configList) {
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            cacheKeys.add(CacheConstants.SYS_CONFIG_KEY + config.getConfigKey());
        }

        sysConfigRepository.deleteByConfigId(configIds);
        redisCache.deleteObject(cacheKeys);
    }

    /**
     * 刷新配置缓存
     */
    @PostConstruct
    @Override
    public void refreshConfigCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);

        List<SysConfig> configsList = sysConfigRepository.selectConfigList();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + config.getConfigKey(), config.getConfigValue());
        }
    }

}
