package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.querydsl.expressions.MySQLExpressions;
import com.ruoyi.system.entity.dsl.QSysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表 数据层实现
 *
 * @author ruoyi
 */
@Transactional
@Repository
public class SysDeptRepositoryImpl implements SysDeptRepository {

    private final QSysDept sysDept = new QSysDept("a");

    @Resource
    private MySQLQueryFactory queryFactory;

    /**
     * 新增部门
     */
    @Override
    public long insertDept(com.ruoyi.system.entity.SysDept entity) {
        return queryFactory.insert(sysDept)
                .populate(entity, BeanMapper.DEFAULT)
                .execute();
    }

    /**
     * 修改部门
     */
    @Override
    public long updateDept(com.ruoyi.system.entity.SysDept entity) {
        return queryFactory.update(sysDept)
                .populate(entity, BeanMapper.DEFAULT)
                .where(sysDept.deptId.eq(entity.getDeptId()))
                .execute();
    }

    /**
     * 修改子元素关系
     */
    @Override
    public void updateDeptAncestor(List<com.ruoyi.system.entity.SysDept> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }

        SQLUpdateClause update = queryFactory.update(sysDept);

        for (com.ruoyi.system.entity.SysDept entity : entityList) {
            update.set(sysDept.ancestor, entity.getAncestor())
                    .where(sysDept.deptId.eq(entity.getDeptId()))
                    .addBatch();
        }

        update.execute();
    }

    /**
     * 根据部门ID删除部门
     */
    @Override
    public long deleteDeptById(Long deptId) {
        return queryFactory.delete(sysDept)
                .where(sysDept.deptId.eq(deptId))
                .execute();
    }

    /**
     * 根据条件查询部门列表
     */
    @Override
    public List<com.ruoyi.system.entity.SysDept> selectDeptList(SysDeptQuery query) {
        BooleanBuilder builder = new BooleanBuilder();

        // 部门名称
        if (StringUtils.isNotBlank(query.getDeptName())) {
            builder.and(sysDept.deptName.like(query.getDeptName()));
        }
        // 部门状态
        if (StringUtils.isNotBlank(query.getStatus())) {
            builder.and(sysDept.status.eq(query.getStatus()));
        }

        return queryFactory.selectFrom(sysDept)
                .orderBy(sysDept.parentId.asc(), sysDept.orderNum.asc())
                .fetch();
    }

    /**
     * 根据部门ID查询所有子部门
     */
    @Override
    public List<com.ruoyi.system.entity.SysDept> selectChildrenByDeptId(Long deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(MySQLExpressions.findInSet(deptId, sysDept.ancestor))
                .fetch();
    }

    /**
     * 根据部门ID查询
     */
    @Override
    public com.ruoyi.system.entity.SysDept selectByDeptId(Long deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptId.eq(deptId))
                .fetchFirst();
    }

    /**
     * 校验部门名称是否唯一
     */
    @Override
    public com.ruoyi.system.entity.SysDept selectByDeptNameUnique(String deptName, Long parentId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptName.eq(deptName))
                .where(sysDept.parentId.eq(parentId))
                .fetchFirst();
    }

    /**
     * 根据上级部门ID查询是否存在
     */
    @Override
    public boolean existByParentId(Long parentId) {
        Long count = queryFactory.select(sysDept.count())
                .from(sysDept)
                .where(sysDept.parentId.eq(parentId))
                .fetchFirst();
        return count > 0;
    }

    @Override
    public boolean existUserByDeptId(Long deptId) {
        return false;
    }

}
