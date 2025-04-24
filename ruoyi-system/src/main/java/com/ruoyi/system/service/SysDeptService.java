package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.vo.SysDeptVO;

import java.util.List;

/**
 * 部门管理 服务层
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
    List<SysDeptVO> selectDeptExcludeList(String deptId);

    /**
     * 查询部门信息
     */
    SysDeptVO selectDeptById(String deptId);

    /**
     * 新增部门
     */
    void insertDept(SysDeptInsertDTO dto);

    /**
     * 修改部门
     */
    void updateDept(SysDeptUpdateDTO dto);

    /**
     * 删除部门
     */
    void deleteDeptById(String deptId);

    /**
     * 查询部门树结构信息
     */
    List<TreeSelect> selectDeptTreeList(SysDeptQuery query);

    /**
     * 根据角色ID查询部门树信息
     */
    List<String> selectDeptListByRoleId(Long roleId);

    /**
     * 校验部门是否有数据权限
     */
    void checkDeptDataScope(String deptId);

}
