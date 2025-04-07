package com.ruoyi.framework.jpa.utils;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;

public class PageableUtils {

    private static final int MAX_PAGE_NUMBER = Integer.MAX_VALUE;

    private static final int MAX_PAGE_SIZE = 2000;

    private static int parseAndApplyBoundaries(int parameter, int upper, boolean shiftIndex) {
        int parse = parameter - (shiftIndex ? 1 : 0);

        return parse < 0 ? 0 : Math.min(parse, upper);
    }

    public static Pageable parse(int pageNumber, int pageSize, OrderSpecifier<?>... orderSpecifiers) {
        int pn = parseAndApplyBoundaries(pageNumber, MAX_PAGE_NUMBER, true);
        int ps = parseAndApplyBoundaries(pageSize, MAX_PAGE_SIZE, false);

        return QPageRequest.of(pn, ps, orderSpecifiers);
    }

}
