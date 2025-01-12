package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.vo.SysConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 配置表 转换层
 *
 * @author ruoyi
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysConfigConverter {

    SysConfig toSysConfig(SysConfigDTO dto);

    SysConfigVO toSysConfigVO(SysConfig item);

}
