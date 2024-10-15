package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.vo.SysDeptVO;

import java.util.List;

/**
 * 部门表 服务层
 *
 * @author ruoyi
 */
public interface SysDeptService {

    /**
     * 根据条件查询部门列表
     */
    List<SysDept> selectDeptList(SysDeptQuery query);

    /**
     * 根据部门ID查询详情
     */
    SysDeptVO selectDeptById(Long deptId);

    /**
     * 新增部门
     */
    long insertDept(SysDeptInsertDTO dto);

    /**
     * 修改部门
     */
    long updateDept(SysDeptUpdateDTO dto);

    /**
     * 删除部门
     */
    long deleteDept(Long deptId);

}
