package com.ruoyi.framework.querydsl.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

    /**
     * 创建人
     */
    @CreatedBy
    private String createdBy;
    /**
     * 创建日期
     */
    @CreatedDate
    private LocalDateTime createdDate;
    /**
     * 上次修改人
     */
    @LastModifiedBy
    private String lastModifiedBy;
    /**
     * 上次修改日期
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
