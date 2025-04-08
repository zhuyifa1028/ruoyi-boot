package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.vo.SysDeptVO;

import java.util.List;

/**
 * 部门管理 业务层
 *
 * @author ruoyi
 */
public interface SysDeptService {

    /**
     * 根据条件查询部门列表
     */
    List<SysDeptVO> selectDeptList(SysDeptQuery query);

    /**
     * 查询部门列表（排除节点）
     */
    List<SysDeptVO> selectExcludeList(String deptId);

    /**
     * 根据部门ID查询详细信息
     */
    SysDeptVO selectDeptById(String deptId);

    /**
     * 新增部门
     */
    void insertDept(SysDeptDTO dto);

    /**
     * 修改部门
     */
    void updateDept(SysDeptDTO dto);

    /**
     * 删除部门
     */
    void deleteDeptById(String deptId);

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    List<TreeSelect> selectDeptTreeList(SysDeptQuery dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    List<Long> selectDeptListByRoleId(Long roleId);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    void checkDeptDataScope(Long deptId);
}
