package com.ruoyi.framework.manager.factory;

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
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    TimerTask recordLogininfor(final String username, final String status, final String message,
                               final Object... args);

    /**
     * 操作日志记录
     */
    TimerTask recordOper(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult, long costTime);

}
