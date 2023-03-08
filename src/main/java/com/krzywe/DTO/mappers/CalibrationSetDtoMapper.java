package com.krzywe.DTO.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.Model.CalibrationSet;

@Mapper(componentModel = "spring")
public interface CalibrationSetDtoMapper {
	
	public CalibrationSet CalibrationSetDtoToCalibrationSet(CalibrationSetDTO calibrationSetDTO);
	
	public CalibrationSet updateToEntity(@MappingTarget CalibrationSet calibrationSet, CalibrationSetDTO calibrationSetDTO);

}
