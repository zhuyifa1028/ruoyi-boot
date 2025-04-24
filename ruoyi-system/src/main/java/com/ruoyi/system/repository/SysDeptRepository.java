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

    List<SysDept> selectDeptList(SysDeptQuery query);

    SysDept selectDeptById(String deptId);

    SysDept selectDeptNameUnique(String deptName, String parentId);

    void insertDept(SysDept entity);

    void updateDept(SysDept entity);

    void updateDeptChildren(String parentId, String newAncestors, String oldAncestors);

    boolean checkDeptExistChild(String deptId);

    void deleteDeptById(String deptId);

    List<String> selectDeptListByRoleId(Long roleId, Boolean deptCheckStrictly);
}
