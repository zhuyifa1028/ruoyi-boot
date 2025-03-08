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

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity implements Serializable {

    @CreatedBy
    @Column(length = 20)
    @Comment(value = "创建人")
    private String createdBy;

    @CreatedDate
    @Comment(value = "创建日期")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(length = 20)
    @Comment(value = "上次修改人")
    private String lastModifiedBy;

    @LastModifiedDate
    @Comment(value = "上次修改日期")
    private LocalDateTime lastModifiedDate;

}
