package com.krzywe.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.MaterialType;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;

import jakarta.validation.constraints.NotNull;

public interface ICalibrationSetService {

	CalibrationSetView create(CalibrationSetDTO calibrationSetDTO);

	CalibrationSetView update(CalibrationSetDTO calibrationSetDTO, String id) throws EntityNotFoundException;

	CalibrationSetView findById(String id) throws EntityNotFoundException;

	Page<SimpleCalibrationSetView> findAll(String name, MaterialType materialType, LocalDate preparationDateFrom,
			LocalDate preparationDateTo, LocalDateTime createdDateFrom, LocalDateTime createdDateTo, int pageNumber,
			int pageSize, Direction sortDirection, String sortAttribute);

}