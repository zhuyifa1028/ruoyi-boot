package com.ruoyi.system.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.ruoyi.system.entity.QSysConfig.sysConfig;

/**
 * 配置管理 业务实现
 *
 * @author ruoyi
 */
@Transactional(rollbackFor = Exception.class)
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
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getConfigName())) {
            predicate.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StringUtils.isNotBlank(query.getConfigKey())) {
            predicate.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (StringUtils.isNotBlank(query.getConfigType())) {
            predicate.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (ObjectUtils.isNotEmpty(query.getBeginTime())) {
            predicate.and(sysConfig.createTime.goe(LocalDateTime.of(query.getBeginTime(), LocalTime.MIN)));
        }
        if (ObjectUtils.isNotEmpty(query.getEndTime())) {
            predicate.and(sysConfig.createTime.loe(LocalDateTime.of(query.getEndTime(), LocalTime.MAX)));
        }

        Pageable pageable = PageableUtils.parse(query.getPageNumber(), query.getPageSize(), sysConfig.createTime.desc());

        Page<SysConfig> page = sysConfigRepository.findAll(predicate, pageable);

        return page.map(item -> sysConfigConverter.toSysConfigVO(item));
    }

    /**
     * 根据配置ID查询详细信息
     */
    @DataSource(DataSourceType.MASTER)
    @Override
    public SysConfigVO selectConfigById(String configId) {
        Optional<SysConfig> opt = sysConfigRepository.findById(configId);

        return opt.map(item -> sysConfigConverter.toSysConfigVO(item))
                .orElse(null);
    }

    /**
     * 根据配置键查询配置值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = redisCache.getCacheObject(CacheConstants.SYS_CONFIG, configKey);
        if (StringUtils.isNotBlank(configValue)) {
            return configValue;
        }

        SysConfig retConfig = sysConfigRepository.findByConfigKey(configKey);
        if (ObjectUtils.isNotEmpty(retConfig)) {

            redisCache.setCacheObject(CacheConstants.SYS_CONFIG, configKey, retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigDTO dto) {
        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjectUtils.isNotEmpty(check)) {
            throw new ServiceException("新增参数'" + dto.getConfigName() + "'失败，参数键名已存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        entity.markCreated();
        sysConfigRepository.save(entity);

        redisCache.setCacheObject(CacheConstants.SYS_CONFIG, dto.getConfigKey(), dto.getConfigValue());
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigDTO dto) {
        SysConfig info = sysConfigRepository.getReferenceById(dto.getConfigId());
        if (ObjectUtils.isEmpty(info)) {
            throw new ServiceException("修改参数'" + dto.getConfigName() + "'失败，参数信息不存在");
        }

        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjectUtils.isNotEmpty(check) && ObjectUtils.notEqual(check.getConfigId(), dto.getConfigId())) {
            throw new ServiceException("修改参数'" + dto.getConfigName() + "'失败，参数键名已存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        sysConfigRepository.saveAndFlush(entity);

        if (ObjectUtils.notEqual(info.getConfigKey(), dto.getConfigKey())) {
            redisCache.deleteObject(CacheConstants.SYS_CONFIG, info.getConfigKey());
        }
        redisCache.setCacheObject(CacheConstants.SYS_CONFIG, dto.getConfigKey(), dto.getConfigValue());
    }

    /**
     * 批量删除配置
     */
    @Override
    public void deleteConfigByIds(List<String> configIds) {
        List<SysConfig> list = sysConfigRepository.findAllById(configIds);
        for (SysConfig config : list) {
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            sysConfigRepository.delete(config);
            redisCache.deleteObject(CacheConstants.SYS_CONFIG, config.getConfigKey());
        }
    }

    /**
     * 刷新配置缓存
     */
    @PostConstruct
    @Override
    public void refreshConfigCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG + "*");
        redisCache.deleteObject(keys);

        List<SysConfig> configsList = sysConfigRepository.findAll();
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(CacheConstants.SYS_CONFIG, config.getConfigKey(), config.getConfigValue());
        }
    }

}
