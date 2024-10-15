package com.ruoyi.system.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.querydsl.mapper.EntityMapper;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.entity.path.QSysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    public long insertDept(SysDept entity) {
        return queryFactory.insert(sysDept)
                .populate(entity, EntityMapper.INSERT)
                .execute();
    }

    /**
     * 修改部门
     */
    @Override
    public long updateDept(SysDept entity) {
        return queryFactory.update(sysDept)
                .populate(entity, EntityMapper.UPDATE)
                .where(sysDept.deptId.eq(entity.getDeptId()))
                .execute();
    }

    /**
     * 修改子元素关系
     */
    @Override
    public void updateDeptAncestor(List<SysDept> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }

        SQLUpdateClause update = queryFactory.update(sysDept);

        for (SysDept entity : entityList) {
            update  // 修改字段
                    .set(sysDept.ancestor, entity.getAncestor())
                    .set(sysDept.lastModifiedBy, SecurityUtils.getUsername())
                    .set(sysDept.lastModifiedDate, LocalDateTime.now())
                    // 根据部门ID
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
    public List<SysDept> selectDeptList(SysDeptQuery query) {
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
    public List<SysDept> selectChildrenByDeptId(Long deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(Expressions.booleanTemplate("find_in_set({0}, {1}) > 0", deptId, sysDept.ancestor))
                .fetch();
    }

    /**
     * 根据部门ID查询
     */
    @Override
    public SysDept selectByDeptId(Long deptId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptId.eq(deptId))
                .fetchFirst();
    }

    /**
     * 校验部门名称是否唯一
     */
    @Override
    public SysDept selectByDeptNameUnique(String deptName, Long parentId) {
        return queryFactory.selectFrom(sysDept)
                .where(sysDept.deptName.eq(deptName)
                        .and(sysDept.parentId.eq(parentId)))
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
