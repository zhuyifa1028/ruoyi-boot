package com.ruoyi.framework.querydsl.expressions;

import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;

public class MySQLExpressions {

    public static BooleanTemplate findInSet(Object... args) {
        return Expressions.booleanTemplate("find_in_set({0}, {1}) > 0", args);
    }
}
