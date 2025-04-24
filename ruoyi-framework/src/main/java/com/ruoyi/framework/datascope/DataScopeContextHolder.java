package com.ruoyi.framework.datascope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源切换处理
 *
 * @author ruoyi
 */
public class DataScopeContextHolder {

    public static final Logger log = LoggerFactory.getLogger(DataScopeContextHolder.class);

    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源的变量
     */
    public static void setDataScope(String dataScope) {
        log.info("数据权限：{}", dataScope);
        CONTEXT_HOLDER.set(dataScope);
    }

    /**
     * 获得数据源的变量
     */
    public static String getDataScope() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataScope() {
        CONTEXT_HOLDER.remove();
    }

}
