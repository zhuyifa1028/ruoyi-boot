package com.ruoyi.common.exception;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    public ServiceException(String template, Object... params) {
        this.message = StrUtil.format(template, params);
    }

}
