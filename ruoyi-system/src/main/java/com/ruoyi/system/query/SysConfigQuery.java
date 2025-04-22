package com.ruoyi.system.query;

import com.ruoyi.framework.web.domain.AbstractPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ApiModel("SysConfigQuery")
@Data
public class SysConfigQuery extends AbstractPageQuery {

    @ApiModelProperty("参数名称")
    private String configName;

    @ApiModelProperty("系统内置（Y是 N否）")
    private String configType;

    @ApiModelProperty("参数键名")
    private String configKey;

    @ApiModelProperty("创建事件——区间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("创建时间——区间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

}
