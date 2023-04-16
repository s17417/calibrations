package com.krzywe.DTO.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.Model.Analyte;

@Mapper(componentModel = "spring")
public interface AnalyteDtoMapper {
	
public Analyte analyeDTOtoAnalyte(AnalyteDTO analyteDTO);
	
public Analyte updateToEntity(@MappingTarget Analyte analyte, AnalyteDTO analyteDTO);


}
