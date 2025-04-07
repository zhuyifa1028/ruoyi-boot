package com.ruoyi.framework.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Schema(description = "AjaxResult")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResult<T> implements Serializable {

    @Schema(description = "消息状态码")
    private int code;

    @Schema(description = "消息内容")
    private String msg;

    @Schema(description = "列表数据")
    private T data;

    public static AjaxResult<Void> ok() {
        return ok(null);
    }

    public static <T> AjaxResult<T> ok(T data) {
        return ok("操作成功", data);
    }

    public static <T> AjaxResult<T> ok(String msg, T data) {
        return new AjaxResult<>(HttpStatus.OK.value(), msg, data);
    }

    public static AjaxResult<Void> error() {
        return error("操作失败");
    }

    public static AjaxResult<Void> error(String msg) {
        return error(msg, null);
    }

    public static <T> AjaxResult<T> error(String msg, T data) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    public static <T> AjaxResult<T> error(int code, String msg, T data) {
        return new AjaxResult<>(code, msg, data);
    }

}
