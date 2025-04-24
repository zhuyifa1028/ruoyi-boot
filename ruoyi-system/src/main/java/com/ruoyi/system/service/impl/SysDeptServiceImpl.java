package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TreeSelectUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.converter.SysDeptConverter;
import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.entity.SysRole;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import com.ruoyi.system.repository.SysUserRepository;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 部门管理 服务实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptConverter sysDeptConverter;
    @Autowired
    private SysDeptRepository sysDeptRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 根据条件查询部门列表
     */
    @Override
    public List<SysDeptVO> selectDeptList(SysDeptQuery query) {
        List<SysDept> list = sysDeptRepository.selectDeptList(query);

        return sysDeptConverter.toSysDeptVO(list);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @Override
    public List<SysDeptVO> selectDeptExcludeList(String deptId) {
        List<SysDept> list = sysDeptRepository.selectDeptList(new SysDeptQuery());

        if (StringUtils.isNotBlank(deptId)) {
            list.removeIf(d -> Objects.equals(deptId, d.getDeptId()) || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId));
        }

        return sysDeptConverter.toSysDeptVO(list);
    }

    /**
     * 查询部门信息
     */
    @Override
    public SysDeptVO selectDeptById(String deptId) {
        SysDept info = sysDeptRepository.selectDeptById(deptId);
        if (Objects.isNull(info)) {
            throw new ServiceException("部门信息不存在，请检查ID");
        }

        return sysDeptConverter.toSysDeptVO(info);
    }

    /**
     * 新增保存部门信息
     *
     * @param dto 部门信息
     */
    @Override
    public void insertDept(SysDeptInsertDTO dto) {
        SysDept parent = sysDeptRepository.selectDeptById(dto.getParentId());
        if (Objects.isNull(parent)) {
            throw new ServiceException(String.format("新增部门'%s'失败，上级部门不存在", dto.getDeptName()));
        }
        if (ObjectUtils.notEqual(parent.getStatus(), UserConstants.DEPT_NORMAL)) {
            throw new ServiceException(String.format("新增部门'%s'失败，上级部门已停用", dto.getDeptName()));
        }

        SysDept unique = sysDeptRepository.selectDeptNameUnique(dto.getDeptName(), dto.getParentId());
        if (Objects.nonNull(unique)) {
            throw new ServiceException(String.format("新增部门'%s'失败，部门名称已存在", dto.getDeptName()));
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.setDeptId(IdUtils.fastUUID());
        entity.setCreateBy(SecurityUtils.getUsername());
        entity.setCreateTime(LocalDateTime.now());
        entity.setAncestors(parent.getAncestors() + "," + dto.getParentId());
        sysDeptRepository.insertDept(entity);
    }

    /**
     * 修改保存部门信息
     *
     * @param dto 部门信息
     */
    @Override
    public void updateDept(SysDeptUpdateDTO dto) {
        SysDept info = sysDeptRepository.selectDeptById(dto.getDeptId());
        if (Objects.isNull(info)) {
            throw new ServiceException("部门信息不存在，请检查ID");
        }
        if (Objects.equals(dto.getDeptId(), dto.getParentId())) {
            throw new ServiceException(String.format("修改部门'%s'失败，上级部门不能是自己", dto.getDeptName()));
        }

        SysDept parent = sysDeptRepository.selectDeptById(dto.getParentId());
        if (Objects.isNull(parent)) {
            throw new ServiceException(String.format("修改部门'%s'失败，上级部门不存在", dto.getDeptName()));
        }

        SysDept unique = sysDeptRepository.selectDeptNameUnique(dto.getDeptName(), dto.getParentId());
        if (Objects.nonNull(unique) && ObjectUtils.notEqual(dto.getDeptId(), unique.getDeptId())) {
            throw new ServiceException(String.format("修改部门'%s'失败，部门名称已存在", dto.getDeptName()));
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.setUpdateBy(SecurityUtils.getUsername());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setAncestors(parent.getAncestors() + "," + dto.getParentId());
        sysDeptRepository.updateDept(entity);

        sysDeptRepository.updateDeptChildren(entity.getDeptId(), entity.getAncestors(), info.getAncestors());
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     */
    @Override
    public void deleteDeptById(String deptId) {
        if (sysDeptRepository.checkDeptExistChild(deptId)) {
            throw new ServiceException("存在下级部门，不允许删除");
        }
        if (sysUserRepository.checkDeptExistUser(deptId)) {
            throw new ServiceException("部门存在用户，不允许删除");
        }
        sysDeptRepository.deleteDeptById(deptId);
    }

    /**
     * 查询部门树结构信息
     *
     * @param query 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptQuery query) {
        List<SysDeptVO> voList = SpringUtils.getAopProxy(this).selectDeptList(query);

        return TreeSelectUtils.build(voList, item -> {
            TreeSelect ts = new TreeSelect();
            ts.setId(item.getDeptId());
            ts.setParentId(item.getParentId());
            ts.setLabel(item.getDeptName());
            ts.setDisabled(StringUtils.equals(UserConstants.DEPT_DISABLE, item.getStatus()));
            return ts;
        });
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<String> selectDeptListByRoleId(Long roleId) {
        SysRole role = sysRoleRepository.selectRoleById(roleId);
        return sysDeptRepository.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(String deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId)) {
            SysDeptQuery dept = new SysDeptQuery();
            dept.setDeptId(deptId);
            List<SysDeptVO> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

}
