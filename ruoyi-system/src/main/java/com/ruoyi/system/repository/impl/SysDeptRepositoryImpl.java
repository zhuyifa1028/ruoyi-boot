package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.mysql.MySQLQuery;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.datascope.DataScopeContextHolder;
import com.ruoyi.system.entity.QSysDept;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruoyi.system.entity.QSysRoleDept.sysRoleDept;

/**
 * 部门表 数据层实现
 *
 * @author ruoyi
 */
@Repository
@Transactional
public class SysDeptRepositoryImpl implements SysDeptRepository {

    public static final QSysDept sysDept = new QSysDept("d");

    @Autowired
    private MySQLQueryFactory queryFactory;

    @DataScope(deptAlias = "d")
    @Override
    public List<SysDept> selectDeptList(SysDeptQuery query) {

        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getDeptId())) {
            predicate.and(sysDept.deptId.eq(query.getDeptId()));
        }
        if (StringUtils.isNotBlank(query.getParentId())) {
            predicate.and(sysDept.parentId.eq(query.getParentId()));
        }
        if (StringUtils.isNotBlank(query.getDeptName())) {
            predicate.and(sysDept.deptName.contains(query.getDeptName()));
        }
        if (StringUtils.isNotEmpty(query.getStatus())) {
            predicate.and(sysDept.status.eq(query.getStatus()));
        }
        predicate.and(sysDept.delFlag.eq("0"));
        predicate.and(Expressions.booleanTemplate(" 1 = {0} " + DataScopeContextHolder.getDataScope(), 1));

        return queryFactory.selectFrom(sysDept)
                .where(predicate)
                .orderBy(sysDept.parentId.asc(), sysDept.orderNum.asc())
                .fetch();
    }

    @Override
    public SysDept selectDeptById(String deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptId.eq(deptId)
                        .and(sysDept.delFlag.eq("0"))
                )
                .fetchFirst();
    }

    @Override
    public SysDept selectDeptNameUnique(String deptName, String parentId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptName.eq(deptName)
                        .and(sysDept.parentId.eq(parentId))
                        .and(sysDept.delFlag.eq("0"))
                )
                .fetchFirst();
    }

    @Override
    public void insertDept(SysDept entity) {
        queryFactory.insert(sysDept)
                .populate(entity)
                .execute();
    }

    @Override
    public void updateDept(SysDept entity) {
        queryFactory.update(sysDept)
                .populate(entity)
                .where(sysDept.deptId.eq(entity.getDeptId())
                        .and(sysDept.delFlag.eq("0"))
                )
                .execute();
    }

    @Override
    public void updateDeptChildren(String parentId, String newAncestors, String oldAncestors) {
        queryFactory.update(sysDept)
                .set(sysDept.ancestors, Expressions.stringTemplate("replace({0}, {1}, {2})", sysDept.ancestors, oldAncestors, newAncestors))
                .where(Expressions.booleanTemplate("find_in_set({0}, {1}) > 0", parentId, sysDept.ancestors)
                        .and(sysDept.delFlag.eq("0"))
                )
                .execute();
    }

    @Override
    public boolean checkDeptExistChild(String deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.parentId.eq(deptId)
                        .and(sysDept.delFlag.eq("0"))
                )
                .fetchCount() > 0;
    }

    @Override
    public void deleteDeptById(String deptId) {
        queryFactory.update(sysDept)
                .set(sysDept.delFlag, "2")
                .where(sysDept.deptId.eq(deptId))
                .execute();
    }

    @Override
    public List<String> selectDeptListByRoleId(Long roleId, Boolean deptCheckStrictly) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysRoleDept.roleId.eq(roleId));

        if (deptCheckStrictly) {
            MySQLQuery<String> subQuery = queryFactory.select(sysDept.parentId)
                    .from(sysDept)
                    .innerJoin(sysRoleDept).on(sysRoleDept.deptId.eq(sysDept.deptId))
                    .where(sysRoleDept.roleId.eq(roleId));

            predicate.and(sysRoleDept.deptId.notIn(subQuery));
        }

        return queryFactory.select(sysDept.deptId)
                .from(sysDept)
                .leftJoin(sysRoleDept).on(sysRoleDept.deptId.eq(sysDept.deptId))
                .where(sysRoleDept.roleId.eq(roleId))
                .fetch();
    }

}
