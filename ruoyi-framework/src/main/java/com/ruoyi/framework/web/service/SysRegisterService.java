package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.model.RegisterBody;

/**
 * 注册校验方法
 *
 * @author ruoyi
 */
public interface SysRegisterService {

    /**
     * 注册
     */
    String register(RegisterBody registerBody);

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    void validateCaptcha(String username, String code, String uuid);
}
