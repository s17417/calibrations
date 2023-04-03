package com.krzywe.DTO.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.Model.CalibrationPoint;

@Mapper(componentModel = "spring")
public interface CalibrationPointDtoMapper {
	
	public CalibrationPoint calibrationPointDTOtoCalibrationPoint(CalibrationPointDTO calibrationPointDTO);
	
	public CalibrationPoint updateToEntity(@MappingTarget CalibrationPoint calibrationPoint, CalibrationPointDTO calibrationPointDTO);

}
