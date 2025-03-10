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

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends PersistableEntity {

    @CreatedBy
    @Column(length = 20, updatable = false)
    @Comment(value = "创建人")
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    @Comment(value = "创建日期")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(length = 20, insertable = false)
    @Comment(value = "上次修改人")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(insertable = false)
    @Comment(value = "上次修改日期")
    private LocalDateTime lastModifiedDate;

}
