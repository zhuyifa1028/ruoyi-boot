package com.ruoyi.system.repository.impl;

import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.ruoyi.system.entity.QSysRole.sysRole;

@Repository
@Transactional
public class SysRoleRepositoryImpl implements SysRoleRepository {

    @Autowired
    private MySQLQueryFactory queryFactory;

    @Override
    public SysRole selectRoleById(Long roleId) {
        return queryFactory.selectFrom(sysRole)
                .where(sysRole.roleId.eq(roleId))
                .fetchFirst();
    }
}
