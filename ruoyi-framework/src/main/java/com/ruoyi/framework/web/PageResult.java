package com.ruoyi.framework.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Schema(description = "PageResult")
@Data
public class PageResult<T> extends AjaxResult<T> {

    @Schema(description = "总记录数")
    private long total;

    public static <T> PageResult<List<T>> ok(Page<T> page) {
        return ok("操作成功", page);
    }

    public static <T> PageResult<List<T>> ok(String msg, Page<T> page) {
        PageResult<List<T>> result = new PageResult<>();
        result.setCode(HttpStatus.OK.value());
        result.setMsg(msg);
        result.setData(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

}
