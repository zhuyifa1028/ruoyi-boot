package com.ruoyi.framework.aspectj;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.framework.manager.AsyncFactory;
import com.ruoyi.framework.manager.AsyncManager;
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

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 计算操作消耗时间
     */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    /**
     * 异步工厂
     */
    @Resource
    private AsyncFactory asyncFactory;

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(Log controllerLog) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
        log.info("{}==>{} start", controllerLog.title(), controllerLog.businessType());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(value = "@annotation(controllerLog)", returning = "jsonResult")
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
            // 消耗时间
            long costTime = System.currentTimeMillis() - TIME_THREADLOCAL.get();
            // 保存数据库
            AsyncManager.me().execute(asyncFactory.recordOper(joinPoint, controllerLog, e, jsonResult, costTime));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
        } finally {
            TIME_THREADLOCAL.remove();
            log.info("{}==>{} end", controllerLog.title(), controllerLog.businessType());
        }
    }

}
