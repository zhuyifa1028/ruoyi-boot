package com.ruoyi.system.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruoyi.system.entity.QSysConfig;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;


@Repository
public class SysConfigRepositoryImpl extends SimpleJpaRepository<SysConfig, Long> implements SysConfigRepository {

    @Resource
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    public SysConfigRepositoryImpl(EntityManager entityManager) {
        super(SysConfig.class, entityManager);
    }

    @Override
    public SysConfig selectByConfigKey(String configKey) {
        return jpaQueryFactory.selectFrom(QSysConfig.sysConfig)
                .where(QSysConfig.sysConfig.configKey.eq(configKey))
                .fetchOne();
    }

}
