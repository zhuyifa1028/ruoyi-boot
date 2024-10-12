package com.ruoyi.framework.querydsl.mapper;

import com.querydsl.core.types.Path;
import com.querydsl.core.util.BeanMap;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.AbstractMapper;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class EntityMapper extends AbstractMapper<Object> {

    public static final EntityMapper INSERT = new EntityMapper(true, false);

    public static final EntityMapper UPDATE = new EntityMapper(false, true);

    private final boolean forInsert;

    private final boolean forUpdate;

    @Override
    public Map<Path<?>, Object> createMap(RelationalPath<?> entity, Object object) {
        Map<Path<?>, Object> valueMap = new LinkedHashMap<>();

        BeanMap beanMap = new BeanMap(object);
        Map<String, Path<?>> pathMap = getColumns(entity);

        pathMap.forEach((name, path) -> {

            if (beanMap.containsKey(name)) {
                Object value = beanMap.get(name);

                if (Objects.nonNull(value)) {
                    valueMap.put(path, value);
                } else {
                    // 当前字段信息
                    Field field = ReflectionUtils.findField(object.getClass(), name);
                    if (Objects.isNull(field)) {
                        return;
                    }

                    if (forInsert) {
                        // 创建人
                        if (field.isAnnotationPresent(CreatedBy.class)) {
                            valueMap.put(path, SecurityUtils.getUsername());
                        }
                        // 创建日期
                        if (field.isAnnotationPresent(CreatedDate.class)) {
                            valueMap.put(path, LocalDateTime.now());
                        }
                    }

                    if (forUpdate) {
                        // 上次修改人
                        if (field.isAnnotationPresent(LastModifiedBy.class)) {
                            valueMap.put(path, SecurityUtils.getUsername());
                        }
                        // 上次修改日期
                        if (field.isAnnotationPresent(LastModifiedDate.class)) {
                            valueMap.put(path, LocalDateTime.now());
                        }
                    }
                }
            }
        });

        return valueMap;
    }

}
