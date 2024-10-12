package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.framework.querydsl.mapper.EntityMapper;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.entity.path.SysConfigPath;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Component
public class SysConfigRepositoryImpl implements SysConfigRepository {

    private final SysConfigPath sysConfig = new SysConfigPath("a");

    @Resource
    private MySQLQueryFactory queryFactory;

    @Override
    public void insert(SysConfig entity) {
        queryFactory.insert(sysConfig)
                .populate(entity, EntityMapper.INSERT)
                .execute();
    }

    @Override
    public void update(SysConfig entity) {
        queryFactory.update(sysConfig)
                .populate(entity, EntityMapper.UPDATE)
                .where(sysConfig.configId.eq(entity.getConfigId()))
                .execute();
    }

    @Override
    public void delete(SysConfig entity) {
        queryFactory.delete(sysConfig)
                .where(sysConfig.configId.eq(entity.getConfigId()))
                .execute();
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
    public List<SysConfig> selectByConfigId(List<Long> configIds) {
        return queryFactory.selectFrom(sysConfig)
                .where(sysConfig.configId.in(configIds))
                .fetch();
    }

    @Override
    public List<SysConfig> selectConfigList() {
        return queryFactory.selectFrom(sysConfig)
                .fetch();
    }

    @Override
    public Page<SysConfig> selectConfigList(SysConfigQuery query) {
        BooleanBuilder builder = new BooleanBuilder();

        QueryResults<SysConfig> page = queryFactory.selectFrom(sysConfig)
                .where(builder)
                .offset(query.getOffset())
                .limit(query.getLimit())
                .fetchResults();

        return new PageImpl<>(page.getResults(), query.getPageable(), page.getTotal());
    }

}
