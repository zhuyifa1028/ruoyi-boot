package com.ruoyi.framework.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    @Comment(value = "创建人")
    @Column(length = 20, updatable = false)
    @CreatedBy
    private String createdBy;

    @Comment(value = "创建日期")
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Comment(value = "上次修改人")
    @Column(length = 20, insertable = false)
    @LastModifiedBy
    private String lastModifiedBy;

    @Comment(value = "上次修改日期")
    @Column(insertable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
