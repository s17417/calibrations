package com.krzywe.Service;

import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.SimpleCalibrationPointView;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface ICalibrationPointService {

	//create
	Collection<CalibrationPointView> create(String calibrationSetId, List<CalibrationPointDTO> calibrationPointDTOs)
			throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException;

	//update
	SimpleCalibrationPointView update(String calibrationPointId, CalibrationPointDTO calibrationPointDTO)
			throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException;

	CalibrationPointView getById(String calibrationPointId) throws EntityNotFoundException;

	List<CalibrationPointView> getAllByCalibrationSetId(String calibrationSetId);

	void delete(String calibrationPointId);

}