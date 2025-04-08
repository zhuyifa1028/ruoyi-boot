package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.BooleanBuilder;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.converter.SysDeptConverter;
import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruoyi.system.entity.QSysDept.sysDept;

/**
 * 部门管理 业务实现
 *
 * @author ruoyi
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptConverter sysDeptConverter;
    @Resource
    private SysDeptRepository sysDeptRepository;

    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private SysRoleMapper roleMapper;

    /**
     * 根据条件查询部门列表
     */
    @DataScope(deptAlias = "d")
    @Override
    public List<SysDeptVO> selectDeptList(SysDeptQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (StrUtil.isNotBlank(query.getDeptName())) {
            predicate.and(sysDept.deptName.contains(query.getDeptName()));
        }
        if (ObjUtil.isNotEmpty(query.getStatus())) {
            predicate.and(sysDept.status.eq(query.getStatus()));
        }

        Iterable<SysDept> iterable = sysDeptRepository.findAll(predicate);

        return sysDeptConverter.toSysDeptVO(iterable);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @Override
    public List<SysDeptVO> selectExcludeList(String deptId) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysDept.deptId.ne(deptId));
        predicate.and(sysDept.ancestors.notLike("%" + deptId + "%"));

        Iterable<SysDept> iterable = sysDeptRepository.findAll(predicate);

        return sysDeptConverter.toSysDeptVO(iterable);
    }

    /**
     * 根据部门ID查询详细信息
     */
    @Override
    public SysDeptVO selectDeptById(String deptId) {
        checkDeptDataScope(Long.valueOf(deptId));

        SysDept dept = sysDeptRepository.getReferenceById(deptId);
        if (ObjUtil.isNull(dept)) {
            throw new ServiceException("部门信息不存在，请检查部门ID");
        }

        return sysDeptConverter.toSysDeptVO(dept);
    }

    /**
     * 新增部门
     */
    @Override
    public void insertDept(SysDeptDTO dto) {
        SysDept check = sysDeptRepository.findByParentIdAndDeptName(dto.getParentId(), dto.getDeptName());
        if (ObjUtil.isNotNull(check)) {
            throw new ServiceException("新增部门'{}'失败，部门名称已存在", dto.getDeptName());
        }

        SysDept parent = sysDeptRepository.getReferenceById(dto.getParentId());
        if (ObjUtil.isNull(parent)) {
            throw new ServiceException("新增部门'{}'失败，上级部门不存在", dto.getDeptName());
        }
        if (ObjUtil.notEqual(parent.getStatus(), UserConstants.DEPT_NORMAL)) {
            throw new ServiceException("新增部门'{}'失败，上级部门已停用", dto.getDeptName());
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.markCreated();
        entity.setStatus(UserConstants.DEPT_NORMAL);
        entity.setAncestors(parent.getAncestors() + "," + parent.getDeptId());
        sysDeptRepository.save(entity);
    }

    /**
     * 修改部门
     */
    @Override
    public void updateDept(SysDeptDTO dto) {
        if (StrUtil.equals(dto.getDeptId(), dto.getParentId())) {
            throw new ServiceException("修改部门'{}'失败，上级部门不能是自己", dto.getDeptName());
        }

        checkDeptDataScope(Long.valueOf(dto.getDeptId()));

        SysDept dept = sysDeptRepository.getReferenceById(dto.getDeptId());
        if (ObjUtil.isNull(dept)) {
            throw new ServiceException("部门信息不存在，请检查部门ID");
        }

        SysDept check = sysDeptRepository.findByParentIdAndDeptName(dto.getParentId(), dto.getDeptName());
        if (ObjUtil.isNotNull(check) && ObjUtil.notEqual(check.getDeptId(), dept.getDeptId())) {
            throw new ServiceException("修改部门'{}'失败，部门名称已存在", dto.getDeptName());
        }

        SysDept parent = sysDeptRepository.getReferenceById(dto.getParentId());
        if (ObjUtil.isNull(parent)) {
            throw new ServiceException("修改部门'{}'失败，上级部门不存在", dto.getDeptName());
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.setAncestors(parent.getAncestors() + "," + parent.getDeptId());
        // 修改子元素关系
        updateDeptChildren(entity.getDeptId(), entity.getAncestors(), dept.getAncestors());
        // 保存
        sysDeptRepository.saveAndFlush(entity);
    }

    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = sysDeptRepository.findAllByAncestorsContains(deptId);
        if (CollUtil.isEmpty(children)) {
            return;
        }

        for (SysDept dept : children) {
            dept.setAncestors(StrUtil.replaceFirst(dept.getAncestors(), oldAncestors, newAncestors));
        }
        sysDeptRepository.saveAllAndFlush(children);
    }

    /**
     * 删除部门
     */
    @Override
    public void deleteDeptById(String deptId) {
        checkDeptDataScope(Long.valueOf(deptId));

        if (sysDeptRepository.existsByParentId(deptId)) {
            throw new ServiceException("存在下级部门，不允许删除");
        }
        //if (deptService.checkDeptExistUser(deptId)) {
        //    AjaxResult.error("部门存在用户,不允许删除");
        //    return;
        //}
        sysDeptRepository.deleteById(deptId);
    }

    /**
     * 查询部门树结构信息
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptQuery query) {
        List<SysDeptVO> voList = selectDeptList(query);

        List<TreeSelect> collect = voList.stream()
                .map(dept -> {
                    TreeSelect ts = new TreeSelect();
                    ts.setParentId(dept.getParentId());
                    ts.setId(dept.getDeptId());
                    ts.setLabel(dept.getDeptName());
                    ts.setDisabled(ObjUtil.equals(UserConstants.DEPT_DISABLE, dept.getStatus()));
                    return ts;
                })
                .toList();

        return TreeUtil.build(collect, 0L, TreeSelect::getId, TreeSelect::getParentId, TreeSelect::setChildren);
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);

        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId)) {
            //SysDept dept = new SysDept();
            //dept.setDeptId(deptId);
            List<SysDeptVO> voList = selectDeptList(new SysDeptQuery());
            if (StringUtils.isEmpty(voList)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

}
