package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
@ApiModel("R")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {

    @ApiModelProperty("状态码")
    private int status;

    @ApiModelProperty("状态消息")
    private String message;

    @ApiModelProperty("数据对象")
    private T data;

    @ApiModelProperty("数据总数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public static R<Void> ok() {
        return ok("操作成功", null, null);
    }

    public static <T> R<List<T>> ok(Page<T> page) {
        return ok("操作成功", page.getContent(), page.getTotalElements());
    }

    public static <T> R<T> ok(T data) {
        return ok("操作成功", data, null);
    }

    public static <T> R<T> ok(String message, T data, Long total) {
        return new R<>(HttpStatus.OK.value(), message, data, total);
    }

    public static <T> R<T> fail(String message) {
        return fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static <T> R<T> fail(int status, String message) {
        return new R<>(status, message, null, null);
    }

}
