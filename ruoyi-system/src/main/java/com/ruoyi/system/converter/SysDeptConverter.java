package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysDeptDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.vo.SysDeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDeptConverter {

    SysDept toSysDept(SysDeptDTO item);

    SysDeptVO toSysDeptVO(SysDept item);

    List<SysDeptVO> toSysDeptVO(Iterable<SysDept> iterable);

}
