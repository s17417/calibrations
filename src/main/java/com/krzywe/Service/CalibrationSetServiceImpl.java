package com.krzywe.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.DTO.mappers.CalibrationSetDtoMapper;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;
import com.krzywe.Repository.CalibrationSetRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Specifications.ICalibrationSetSpec;

import jakarta.validation.constraints.NotNull;

@Service
public class CalibrationSetServiceImpl implements ICalibrationSetService {
	
	private CalibrationSetRepository calibrationSetRepository;
	
	private CalibrationSetDtoMapper calibrationSetDtoMapper;
	
	private ProjectionFactory projectionFactory;
	
	private ICalibrationSetSpec specification;
	
	public CalibrationSetServiceImpl(
			CalibrationSetRepository calibrationSetRepository,
			CalibrationSetDtoMapper calibrationSetDtoMapper,
			ProjectionFactory projectionFactory,
			ICalibrationSetSpec specification
			) {
		this.calibrationSetRepository = calibrationSetRepository;
		this.calibrationSetDtoMapper = calibrationSetDtoMapper;
		this.projectionFactory = projectionFactory;
		this.specification=specification;
	}

	@Override
	public CalibrationSetView create(@NotNull CalibrationSetDTO calibrationSetDTO) {
		var entity = calibrationSetDtoMapper.CalibrationSetDtoToCalibrationSet(calibrationSetDTO);
		var saveEntity = calibrationSetRepository.save(entity);
		return projectionFactory
				.createProjection(CalibrationSetView.class, saveEntity);	
	}
	
	@Override
	public CalibrationSetView update(@NotNull CalibrationSetDTO calibrationSetDTO,
			@NotNull String id) throws EntityNotFoundException {
		var entity = calibrationSetRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(CalibrationSet.class));
		var mappedEntity = calibrationSetDtoMapper.updateToEntity(entity, calibrationSetDTO);
		var updatedEntity = calibrationSetRepository.save(mappedEntity);
		
		return projectionFactory
				.createProjection(CalibrationSetView.class, updatedEntity);
	}
	
	@Override
	public CalibrationSetView findById(@NotNull String id) throws EntityNotFoundException {
		return calibrationSetRepository
				.findByIdAsView(id)
				.orElseThrow(() -> new EntityNotFoundException(CalibrationSet.class));
	}
	
	@Override
	public Page<SimpleCalibrationSetView> findAll(
			String name,
			MaterialType materialType,
			LocalDate preparationDateFrom,
			LocalDate preparationDateTo,
			LocalDateTime createdDateFrom,
			LocalDateTime createdDateTo,
			int pageNumber,
			int pageSize,
			Direction sortDirection,
			String sortAttribute
			) {
		return calibrationSetRepository.findBy(
				specification.getAllSpecificationsChain(
						name,
						materialType, 
						createdDateFrom, 
						createdDateTo, 
						preparationDateFrom, 
						preparationDateTo
						),
				obj -> obj.as(SimpleCalibrationSetView.class)
				.page(PageRequest.of(
						pageNumber,
						pageSize,
						sortDirection,
						sortAttribute
						)
					)
				);
	}
	
	

}
