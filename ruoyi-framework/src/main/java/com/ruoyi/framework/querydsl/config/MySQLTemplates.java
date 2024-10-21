package com.ruoyi.framework.querydsl.config;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLSerializer;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.querydsl.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MySQLTemplates extends com.querydsl.sql.MySQLTemplates {

    @Override
    public void serializeInsert(QueryMetadata metadata, RelationalPath<?> entity, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery, SQLSerializer context) {
        for (Path<?> column : entity.getColumns()) {
            // 已存在
            if (columns.contains(column)) {
                continue;
            }
            // 字段名
            String name = column.getMetadata().getName();
            // 创建人
            if (ObjectUtils.equal(name, BaseEntity.CREATED_BY)) {
                columns.add(column);
                values.add(ConstantImpl.create(SecurityUtils.getUsername()));
            }
            // 创建日期
            if (ObjectUtils.equal(name, BaseEntity.CREATED_DATE)) {
                columns.add(column);
                values.add(ConstantImpl.create(LocalDateTime.now()));
            }
        }

        super.serializeInsert(metadata, entity, columns, values, subQuery, context);
    }

    @Override
    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity, Map<Path<?>, Expression<?>> updates, SQLSerializer context) {
        for (Path<?> column : entity.getColumns()) {
            // 已存在
            if (updates.containsKey(column)) {
                continue;
            }
            // 字段名
            String name = column.getMetadata().getName();
            // 上次修改人
            if (ObjectUtils.equal(name, BaseEntity.LAST_MODIFIED_BY)) {
                updates.put(column, ConstantImpl.create(SecurityUtils.getUsername()));
            }
            // 上次修改日期
            if (ObjectUtils.equal(name, BaseEntity.LAST_MODIFIED_DATE)) {
                updates.put(column, ConstantImpl.create(LocalDateTime.now()));
            }
        }

        super.serializeUpdate(metadata, entity, updates, context);
    }

}
