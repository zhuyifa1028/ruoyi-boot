package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysRole;

/**
 * 部门表 数据层
 *
 * @author ruoyi
 */
public interface SysRoleRepository {

    SysRole selectRoleById(Long roleId);
}
