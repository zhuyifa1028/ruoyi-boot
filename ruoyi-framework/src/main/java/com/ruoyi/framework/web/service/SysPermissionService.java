package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
public interface SysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser user);

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser user);
}
