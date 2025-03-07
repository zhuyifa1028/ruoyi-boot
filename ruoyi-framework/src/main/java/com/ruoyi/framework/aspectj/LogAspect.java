package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import jakarta.annotation.Resource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    @Resource
    private AsyncFactory asyncFactory;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(Log controllerLog) {
        log.debug("{} start", controllerLog.title());
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 保存数据库
            AsyncManager.me().execute(asyncFactory.recordOper(joinPoint, controllerLog, e, jsonResult, TIME_THREADLOCAL.get()));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:", e);
        } finally {
            TIME_THREADLOCAL.remove();
        }
    }

}
