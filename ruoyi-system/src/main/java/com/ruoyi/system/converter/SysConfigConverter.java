package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.vo.SysConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysConfigConverter {

    SysConfig toSysConfig(SysConfigInsertDTO item);

    SysConfig toSysConfig(SysConfigUpdateDTO item);

    SysConfigVO toSysConfigVO(SysConfig item);

}
