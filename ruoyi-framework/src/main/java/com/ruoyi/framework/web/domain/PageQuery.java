package com.ruoyi.framework.web.domain;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    private int pageNumber;

    private int pageSize;

    public long getOffset() {
        return getPageable().getOffset();
    }

    public long getLimit() {
        return getPageable().getPageSize();
    }

    public Pageable getPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
