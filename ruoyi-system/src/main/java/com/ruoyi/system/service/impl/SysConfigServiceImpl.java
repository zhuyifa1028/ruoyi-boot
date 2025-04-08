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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

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
        if (StrUtil.isNotBlank(query.getConfigName())) {
            predicate.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StrUtil.isNotBlank(query.getConfigKey())) {
            predicate.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (StrUtil.isNotBlank(query.getConfigType())) {
            predicate.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (ObjUtil.isNotNull(query.getBeginTime())) {
            predicate.and(sysConfig.createTime.goe(LocalDateTime.of(query.getBeginTime(), LocalTime.MIN)));
        }
        if (ObjUtil.isNotNull(query.getEndTime())) {
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
        SysConfig config = sysConfigRepository.getReferenceById(configId);
        if (ObjUtil.isNull(config)) {
            throw new ServiceException("未找到信息，请检查配置ID");
        }

        return sysConfigConverter.toSysConfigVO(config);
    }

    /**
     * 根据配置键查询配置值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = redisCache.getCacheObject(CacheConstants.SYS_CONFIG, configKey);
        if (ObjUtil.isNotNull(configValue)) {
            return configValue;
        }

        SysConfig config = sysConfigRepository.findByConfigKey(configKey);
        if (ObjUtil.isNotNull(config)) {

            redisCache.setCacheObject(CacheConstants.SYS_CONFIG, configKey, config.getConfigValue());
            return config.getConfigValue();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StrUtil.isNotBlank(captchaEnabled)) {
            return Convert.toBool(captchaEnabled);
        }
        return false;
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigDTO dto) {
        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjUtil.isNotNull(check)) {
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
        if (ObjUtil.isNull(info)) {
            throw new ServiceException("修改参数'" + dto.getConfigName() + "'失败，参数信息不存在");
        }

        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjUtil.isNotNull(check) && ObjUtil.notEqual(check.getConfigId(), dto.getConfigId())) {
            throw new ServiceException("修改参数'" + dto.getConfigName() + "'失败，参数键名已存在");
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        sysConfigRepository.saveAndFlush(entity);

        if (ObjUtil.notEqual(info.getConfigKey(), dto.getConfigKey())) {
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
            if (StrUtil.equals(UserConstants.YES, config.getConfigType())) {
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
