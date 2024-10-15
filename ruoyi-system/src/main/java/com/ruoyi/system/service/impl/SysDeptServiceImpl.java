package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.system.converter.SysDeptConverter;
import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptConverter sysDeptConverter;
    @Resource
    private SysDeptRepository deptMapper;

    /**
     * 根据条件查询部门列表
     */
    @Override
    public List<SysDept> selectDeptList(SysDeptQuery query) {
        List<SysDept> deptList = deptMapper.selectDeptList(query);
        return sysDeptConverter.toSysDeptVO(deptList);
    }

    /**
     * 根据部门ID查询详情
     */
    @Override
    public SysDeptVO selectDeptById(Long deptId) {
        SysDept dept = deptMapper.selectByDeptId(deptId);
        return sysDeptConverter.toSysDeptVO(dept);
    }

    /**
     * 新增部门
     */
    @Override
    public long insertDept(SysDeptInsertDTO dto) {
        SysDept parent = deptMapper.selectByDeptId(dto.getParentId());
        if (ObjectUtils.isNull(parent)) {
            throw new ServiceException("新增部门'{}'失败，上级部门不存在", dto.getDeptName());
        }
        if (ObjectUtils.notEqual(parent.getStatus(), UserConstants.DEPT_NORMAL)) {
            throw new ServiceException("新增部门'{}'失败，上级部门已停用", dto.getDeptName());
        }

        SysDept unique = deptMapper.selectByDeptNameUnique(dto.getDeptName(), dto.getParentId());
        if (ObjectUtils.isNotNull(unique)) {
            throw new ServiceException("新增部门'{}'失败，部门名称已存在", dto.getDeptName());
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.setAncestor(parent.getAncestor() + "," + parent.getDeptId());
        entity.setStatus(UserConstants.DEPT_NORMAL);
        return deptMapper.insertDept(entity);
    }

    /**
     * 修改部门
     */
    @Override
    public long updateDept(SysDeptUpdateDTO dto) {
        if (ObjectUtils.equal(dto.getDeptId(), dto.getParentId())) {
            throw new ServiceException("修改部门'{}'失败，上级部门不能是自己", dto.getDeptName());
        }

        SysDept parent = deptMapper.selectByDeptId(dto.getParentId());
        if (ObjectUtils.isNull(parent)) {
            throw new ServiceException("修改部门'{}'失败，上级部门不存在", dto.getDeptName());
        }

        SysDept dept = deptMapper.selectByDeptId(dto.getDeptId());
        if (ObjectUtils.isNull(dept)) {
            throw new ServiceException("修改部门'{}'失败，部门不存在", dto.getDeptName());
        }

        SysDept unique = deptMapper.selectByDeptNameUnique(dto.getDeptName(), dto.getParentId());
        if (ObjectUtils.isNotNull(unique) && ObjectUtils.notEqual(unique.getDeptId(), unique.getDeptId())) {
            throw new ServiceException("修改部门'{}'失败，部门名称已存在", dto.getDeptName());
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);

        updateDeptChildren(entity, parent, dept);

        return deptMapper.updateDept(entity);
    }

    /**
     * 修改子元素关系
     */
    public void updateDeptChildren(SysDept entity, SysDept parent, SysDept dept) {
        String newAncestors = parent.getAncestor() + "," + parent.getDeptId();
        String oldAncestors = dept.getAncestor();
        // 祖级列表
        entity.setAncestor(newAncestors);

        List<SysDept> children = deptMapper.selectChildrenByDeptId(entity.getDeptId());
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        for (SysDept child : children) {
            child.setAncestor(child.getAncestor().replaceFirst(oldAncestors, newAncestors));
        }
        deptMapper.updateDeptAncestor(children);
    }

    /**
     * 删除部门
     */
    @Override
    public long deleteDept(Long deptId) {
        SysDept dept = deptMapper.selectByDeptId(deptId);
        if (ObjectUtils.isNull(dept)) {
            throw new ServiceException("删除部门失败，部门不存在");
        }
        if (deptMapper.existByParentId(deptId)) {
            throw new ServiceException("删除部门失败，存在下级部门");
        }
        if (deptMapper.existUserByDeptId(deptId)) {
            throw new ServiceException("删除部门失败，存在用户");
        }

        return deptMapper.deleteDeptById(deptId);
    }

}
