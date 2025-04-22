package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.sql.oracle.OracleQueryFactory;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.entity.QSysConfig;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * 配置表 数据层实现
 *
 * @author ruoyi
 */
@DataSource(DataSourceType.SLAVE)
@Repository
@Transactional
public class SysConfigRepositoryImpl implements SysConfigRepository {

    public static final QSysConfig sysConfig = new QSysConfig("sys_config", "ruoyi");

    @Autowired
    private OracleQueryFactory queryFactory;

    @Override
    public Page<SysConfig> selectConfigList(SysConfigQuery query) {
        Pageable pageable = query.makePageable();

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getConfigName())) {
            predicate.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StringUtils.isNotBlank(query.getConfigType())) {
            predicate.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (StringUtils.isNotBlank(query.getConfigKey())) {
            predicate.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (ObjectUtils.isNotEmpty(query.getStartTime())) {
            predicate.and(sysConfig.createTime.goe(LocalDateTime.of(query.getStartTime(), LocalTime.MIN)));
        }
        if (ObjectUtils.isNotEmpty(query.getEndTime())) {
            predicate.and(sysConfig.createTime.loe(LocalDateTime.of(query.getStartTime(), LocalTime.MAX)));
        }

        QueryResults<SysConfig> fetch = queryFactory.selectFrom(sysConfig)
                .where(predicate)
                .orderBy(sysConfig.createTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(fetch.getResults(), pageable, fetch.getTotal());
    }

    @Override
    public List<SysConfig> selectConfigList() {
        return queryFactory.selectFrom(sysConfig)
                .fetch();
    }

    @Override
    public List<SysConfig> selectByConfigId(List<String> configIds) {
        if (CollectionUtils.isEmpty(configIds)) {
            return Collections.emptyList();
        }

        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configId.in(configIds))
                .fetch();
    }

    @Override
    public SysConfig selectByConfigId(String configId) {
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configId.eq(configId))
                .fetchFirst();
    }

    @Override
    public SysConfig selectByConfigKey(String configKey) {
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configKey.eq(configKey))
                .fetchFirst();
    }

    @Override
    public void insertConfig(SysConfig entity) {
        queryFactory.insert(sysConfig)
                .populate(entity)
                .execute();
    }

    @Override
    public void updateConfig(SysConfig entity) {
        queryFactory.update(sysConfig)
                .populate(entity)
                .where(sysConfig.configId.eq(entity.getConfigId()))
                .execute();
    }

    @Override
    public void deleteByConfigId(List<String> configIds) {
        if (CollectionUtils.isEmpty(configIds)) {
            return;
        }

        queryFactory.delete(sysConfig)
                .where(sysConfig.configId.in(configIds))
                .execute();
    }

}
