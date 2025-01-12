package com.ruoyi.system.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "配置表 Query")
@Data
public class SysConfigQuery implements Serializable {

    private int pageNum;
    private int pageSize;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "创建时间 start")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @Schema(description = "创建时间 end")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

}
