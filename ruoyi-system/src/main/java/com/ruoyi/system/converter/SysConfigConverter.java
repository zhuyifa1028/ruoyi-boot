package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysConfigDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.vo.SysConfigVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SysConfigConverter {

    SysConfigVO toSysConfigVO(SysConfig source);

    SysConfig toSysConfig(SysConfigDTO source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSysConfig(SysConfigDTO source, @MappingTarget SysConfig target);

}
