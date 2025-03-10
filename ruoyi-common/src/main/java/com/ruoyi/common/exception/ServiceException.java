package com.ruoyi.common.exception;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 业务异常
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
public final class ServiceException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    public ServiceException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Object... params) {
        this.message = StrUtil.format(message, params);
    }

}
