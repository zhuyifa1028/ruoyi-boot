package com.ruoyi.framework.manager;

import com.ruoyi.common.annotation.Log;
import org.aspectj.lang.JoinPoint;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public interface AsyncFactory {

    /**
     * 记录登录信息
     */
    TimerTask recordLogininfor(String username, String status, String message, Object... args);

    /**
     * 操作日志记录
     */
    TimerTask recordOper(JoinPoint joinPoint, Log controllerLog, Exception e, Object jsonResult, Long costTime);

}
