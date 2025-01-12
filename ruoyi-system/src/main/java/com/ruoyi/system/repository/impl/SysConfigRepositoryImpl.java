package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.repository.table.SysConfigTable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * 配置表 数据层实现
 *
 * @author ruoyi
 */
@Transactional
@Repository
public class SysConfigRepositoryImpl implements SysConfigRepository {

    private final SysConfigTable sysConfig = new SysConfigTable("tb_1");

    @Resource
    private MySQLQueryFactory queryFactory;

    @Override
    public List<SysConfig> selectAllConfig() {
        return queryFactory.selectFrom(sysConfig)
                .fetch();
    }

    @Override
    public Page<SysConfig> selectByConfigQuery(SysConfigQuery query) {
        BooleanBuilder where = new BooleanBuilder();
        // 配置名称
        if (StringUtils.isNotBlank(query.getConfigName())) {
            where.and(sysConfig.configName.contains(query.getConfigName()));
        }
        // 配置键
        if (StringUtils.isNotBlank(query.getConfigKey())) {
            where.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        // 系统内置
        if (StringUtils.isNotBlank(query.getConfigType())) {
            where.and(sysConfig.configType.eq(query.getConfigType()));
        }
        // 创建时间
        if (ObjectUtils.isNotEmpty(query.getStartTime())) {
            where.and(sysConfig.createTime.goe(LocalDateTime.of(query.getStartTime(), LocalTime.MIN)));
        }
        if (ObjectUtils.isNotEmpty(query.getEndTime())) {
            where.and(sysConfig.createTime.loe(LocalDateTime.of(query.getEndTime(), LocalTime.MAX)));
        }

        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());

        QueryResults<SysConfig> results = queryFactory.selectFrom(sysConfig)
                .where(where)
                .orderBy(sysConfig.createTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<SysConfig> selectByConfigId(Long[] configIds) {
        if (ArrayUtils.isEmpty(configIds)) {
            return Collections.emptyList();
        }
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configId.in(configIds))
                .fetch();
    }

    @Override
    public SysConfig selectByConfigId(Long configId) {
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configId.eq(configId))
                .fetchOne();
    }

    @Override
    public SysConfig selectByConfigKey(String configKey) {
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configKey.eq(configKey))
                .fetchOne();
    }

    @Override
    public void insertConfig(SysConfig config) {
        config.setCreateBy(SecurityUtils.getUsername());
        config.setCreateTime(LocalDateTime.now());

        queryFactory.insert(sysConfig)
                .populate(config, BeanMapper.DEFAULT)
                .execute();
    }

    @Override
    public void updateConfig(SysConfig config) {
        config.setUpdateBy(SecurityUtils.getUsername());
        config.setUpdateTime(LocalDateTime.now());

        queryFactory.update(sysConfig)
                .populate(config, BeanMapper.DEFAULT)
                .where(sysConfig.configId.eq(config.getConfigId()))
                .execute();
    }

    @Override
    public void deleteConfigById(Long configId) {
        queryFactory.delete(sysConfig)
                .where(sysConfig.configId.eq(configId))
                .execute();
    }

}
