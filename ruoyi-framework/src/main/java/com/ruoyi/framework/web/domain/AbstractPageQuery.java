package com.ruoyi.framework.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("AbstractPageQuery")
@Data
public abstract class AbstractPageQuery implements Serializable {

    @ApiModelProperty("页面索引")
    @NotNull(message = "页面索引不能为空")
    @Min(value = 1, message = "页面索引不得小于 1")
    private Integer pageNumber;

    @ApiModelProperty("页面大小")
    @NotNull(message = "页面大小不能为空")
    @Min(value = 1, message = "页面大小不得小于 1")
    private Integer pageSize;

    public Pageable makePageable() {
        return PageRequest.of(--pageNumber, pageSize);
    }

}
