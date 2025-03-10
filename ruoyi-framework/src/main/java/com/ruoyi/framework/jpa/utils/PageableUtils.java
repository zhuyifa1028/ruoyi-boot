package com.ruoyi.framework.jpa.utils;

import com.querydsl.core.types.OrderSpecifier;
import com.ruoyi.common.core.page.PageDomain;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;

public class PageableUtils {

    public static Pageable build(PageDomain query, OrderSpecifier<?> desc) {
        if (ObjectUtils.anyNull(query.getPageNum(), query.getPageSize())) {
            return Pageable.unpaged();
        }
        return PageRequest.of(query.getPageNum() - 1, query.getPageSize(), new QSort(desc));
    }

}
