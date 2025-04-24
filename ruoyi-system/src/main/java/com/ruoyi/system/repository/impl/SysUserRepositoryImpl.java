package com.ruoyi.system.repository.impl;

import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.system.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ruoyi.system.entity.QSysUser.sysUser;

@Repository
@Transactional
public class SysUserRepositoryImpl implements SysUserRepository {

    @Autowired
    private MySQLQueryFactory queryFactory;

    @Override
    public boolean checkDeptExistUser(String deptId) {
        return queryFactory.selectFrom(sysUser)
                .where(sysUser.deptId.eq(deptId)
                        .and(sysUser.delFlag.eq("0"))
                )
                .fetchCount() > 0;
    }
}
