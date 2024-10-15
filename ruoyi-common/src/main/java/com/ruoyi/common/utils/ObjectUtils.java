package com.ruoyi.common.utils;

import java.util.Objects;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    public static boolean isNotNull(Object obj) {
        return Objects.nonNull(obj);
    }

    public static boolean equal(Object a, Object b) {
        return Objects.equals(a, b);
    }
}
