package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;

import java.util.List;

/**
 * 部门表 数据层
 *
 * @author ruoyi
 */
public interface SysDeptRepository {

    /**
     * 新增部门
     */
    long insertDept(SysDept entity);

    /**
     * 修改部门
     */
    long updateDept(SysDept entity);

    /**
     * 修改子元素关系
     */
    void updateDeptAncestor(List<SysDept> entityList);

    /**
     * 根据部门ID删除部门
     */
    long deleteDeptById(Long deptId);

    /**
     * 根据条件查询部门列表
     */
    List<SysDept> selectDeptList(SysDeptQuery query);

    /**
     * 根据部门ID查询所有子部门
     */
    List<SysDept> selectChildrenByDeptId(Long deptId);

    /**
     * 根据部门ID查询
     */
    SysDept selectByDeptId(Long deptId);

    /**
     * 校验部门名称是否唯一
     */
    SysDept selectByDeptNameUnique(String deptName, Long parentId);

    /**
     * 根据上级部门ID查询是否存在
     */
    boolean existByParentId(Long parentId);

    boolean existUserByDeptId(Long deptId);
}
