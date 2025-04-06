package com.ruoyi.framework.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class AuditableEntity extends PersistableEntity {

    @Column(length = 30, updatable = false)
    @Comment(value = "创建者")
    @CreatedBy
    private String createBy;

    @Column(updatable = false)
    @Comment(value = "创建时间")
    @CreatedDate
    private LocalDateTime createTime;

    @Column(length = 30, insertable = false)
    @Comment(value = "更新者")
    @LastModifiedBy
    private String updateBy;

    @Column(insertable = false)
    @Comment(value = "更新时间")
    @LastModifiedDate
    private LocalDateTime updateTime;

}
