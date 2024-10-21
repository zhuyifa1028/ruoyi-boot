package com.ruoyi.framework.querydsl.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建日期
     */
    private LocalDateTime createdDate;
    /**
     * 上次修改人
     */
    private String lastModifiedBy;
    /**
     * 上次修改日期
     */
    private LocalDateTime lastModifiedDate;

}
