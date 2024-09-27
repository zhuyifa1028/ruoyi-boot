package com.ruoyi.framework.querydsl.repository;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.querydsl.entity.BaseEntity;

import java.time.LocalDateTime;

public interface BaseRepository {

    default void beforeInsert(Object entity) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setCreatedBy(SecurityUtils.getUsername());
            ((BaseEntity) entity).setCreatedDate(LocalDateTime.now());
        }
    }

}
