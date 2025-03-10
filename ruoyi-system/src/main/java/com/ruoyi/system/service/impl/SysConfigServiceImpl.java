package com.ruoyi.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.BooleanBuilder;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.jpa.utils.PageableUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static com.ruoyi.system.entity.QSysConfig.sysConfig;

/**
 * 配置表 业务层实现
 *
 * @author ruoyi
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
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
    public void initConfigCache() {
        List<SysConfig> configsList = sysConfigRepository.findAll();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(buildCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 构建缓存键名
     */
    private String buildCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    /**
     * 查询配置列表
     */
    @Override
    public Page<SysConfigVO> selectConfigList(SysConfigQuery query) {
        BooleanBuilder bb = new BooleanBuilder();

        if (StrUtil.isNotBlank(query.getConfigName())) {
            bb.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StrUtil.isNotBlank(query.getConfigKey())) {
            bb.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (StrUtil.isNotBlank(query.getConfigType())) {
            bb.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (ObjectUtils.isNotEmpty(query.getStartDate())) {
            bb.and(sysConfig.createdDate.goe(LocalDateTime.of(query.getStartDate(), LocalTime.MIN)));
        }
        if (ObjectUtils.isNotEmpty(query.getEndDate())) {
            bb.and(sysConfig.createdDate.loe(LocalDateTime.of(query.getEndDate(), LocalTime.MAX)));
        }

        Page<SysConfig> page = sysConfigRepository.findAll(bb, PageableUtils.build(query, sysConfig.createdDate.desc()));

        return page.map(sysConfigConverter::toSysConfigVO);
    }

    /**
     * 查询配置信息
     */
    @DataSource(DataSourceType.MASTER)
    @Override
    public SysConfigVO selectConfigById(String configId) {
        SysConfig config = sysConfigRepository.getReferenceById(configId);

        return sysConfigConverter.toSysConfigVO(config);
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigDTO dto) {
        if (checkConfigKeyExists(dto)) {
            throw new ServiceException("新增配置'{}'失败，配置键名已存在", dto.getConfigName());
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        entity.markCreated();
        sysConfigRepository.save(entity);

        redisCache.setCacheObject(buildCacheKey(dto.getConfigKey()), dto.getConfigValue());
    }

    /**
     * 检查配置键名是否存在
     */
    private boolean checkConfigKeyExists(SysConfigDTO dto) {
        SysConfig info = sysConfigRepository.findByConfigKey(dto.getConfigKey());

        return ObjUtil.isNotNull(info) && ObjUtil.notEqual(info.getConfigId(), dto.getConfigId());
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigDTO dto) {
        SysConfig config = sysConfigRepository.getReferenceById(dto.getConfigId());
        if (ObjUtil.isNull(config)) {
            throw new ServiceException("修改配置'{}'失败，配置信息不存在", dto.getConfigName());
        }
        if (checkConfigKeyExists(dto)) {
            throw new ServiceException("修改配置'{}'失败，配置键名已存在", dto.getConfigName());
        }

        if (ObjUtil.notEqual(config.getConfigKey(), dto.getConfigKey())) {
            redisCache.deleteObject(buildCacheKey(config.getConfigKey()));
        }

        sysConfigConverter.toSysConfig(dto, config);
        sysConfigRepository.saveAndFlush(config);

        redisCache.setCacheObject(buildCacheKey(dto.getConfigKey()), dto.getConfigValue());
    }

    /**
     * 批量删除配置
     */
    @Override
    public void deleteConfigByIds(List<String> configIds) {
        List<SysConfig> configList = sysConfigRepository.findAllById(configIds);

        for (SysConfig config : configList) {
            if (StrUtil.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException("内置配置【{}】不能删除 ", config.getConfigKey());
            }
            sysConfigRepository.delete(config);
            redisCache.deleteObject(buildCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 刷新配置缓存
     */
    @Override
    public void refreshConfigCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);

        initConfigCache();
    }

    /**
     * 查询配置值
     */
    @Override
    public String selectConfigValue(String configKey) {
        String configValue = redisCache.getCacheObject(buildCacheKey(configKey));
        if (StrUtil.isNotBlank(configValue)) {
            return configValue;
        }

        SysConfig config = sysConfigRepository.findByConfigKey(configKey);
        if (ObjUtil.isNotNull(config)) {

            redisCache.setCacheObject(buildCacheKey(configKey), config.getConfigValue());
            return config.getConfigValue();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 查询验证码开关
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigValue("sys.account.captchaEnabled");

        if (StrUtil.isNotBlank(captchaEnabled)) {
            return Convert.toBool(captchaEnabled);
        }
        return false;
    }

}
