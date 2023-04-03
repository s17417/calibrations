package com.krzywe.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.SimpleCalibrationPointView;
import com.krzywe.DTO.mappers.CalibrationPointDtoMapper;
import com.krzywe.Model.CalibrationPoint;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Repository.CalibrationPointRepository;
import com.krzywe.Repository.CalibrationSetRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Service
public class CalibrationPointServiceImpl implements ICalibrationPointService {
	
	private CalibrationPointRepository calibrationPointRepository;
	
	private CalibrationSetRepository calibrationSetRepository;
	
	private CalibrationPointDtoMapper calibrationPointDtoMapper;
	
	private ProjectionFactory projectionFactory;
	
	public CalibrationPointServiceImpl(CalibrationPointRepository calibrationPointRepository,
			CalibrationSetRepository calibrationSetRepository, CalibrationPointDtoMapper calibrationPointDtoMapper,
			ProjectionFactory projectionFactory) {
		super();
		this.calibrationPointRepository = calibrationPointRepository;
		this.calibrationSetRepository = calibrationSetRepository;
		this.calibrationPointDtoMapper = calibrationPointDtoMapper;
		this.projectionFactory = projectionFactory;
	}

	//create
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Collection<CalibrationPointView> create(@NotNull String calibrationSetId, @NotEmpty List<CalibrationPointDTO> calibrationPointDTOs) throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException {
		var calibrationSet = calibrationSetRepository
				.findById(calibrationSetId)
				.orElseThrow(() -> new EntityNotFoundException(CalibrationSet.class));
		
		var calibrationPoints = calibrationPointDTOs
				.parallelStream()
				.map(calibrationPointDtoMapper::calibrationPointDTOtoCalibrationPoint)
				.collect(Collectors.toList());		
		
		//full list of distinct aliases to be added to each calibrationPoint
		var aliasSet = calibrationPoints
				.stream()
				.map(CalibrationPoint::getAliases)
				.map(HashSet::new)
				.collect(() -> new HashSet<String>(),
						HashSet::addAll,
						HashSet::addAll);
		
		// builds list of aliases which exists already as pointId 
		//in this set and calibration points from database. It should be unique
		var exists = calibrationPoints
				.stream()
				.map(CalibrationPoint::getPointId)
				.filter(aliasSet::contains)
				.collect(Collectors.toSet());		
		exists.addAll(
				calibrationPointRepository
				.existsByCalibrationSetIdAndPointId(calibrationSetId, aliasSet)
				);
		
		//if alias exists in pointId there is thrown eception
		if (!exists.isEmpty()) 
			throw new UniquePropertyException(
					CalibrationPoint.class,
					List.of(CalibrationPoint.class.getDeclaredField("aliases"), CalibrationPoint.class.getDeclaredField("pointId")), 
					List.of(exists),
					"Incorect values (already defined in the pointId property for this CalibrationSet)."
					);
		
		calibrationPoints.forEach(calibrationSet::addCalibrationPoint);
		
		var result = calibrationPointRepository.saveAllAndFlush(calibrationPoints);
		return result
				.parallelStream()
				.map(obj -> projectionFactory.createProjection(CalibrationPointView.class, obj))
				.collect(Collectors.toList());				
	}
	
	//update
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public SimpleCalibrationPointView update(@NotNull String calibrationPointId, @NotNull CalibrationPointDTO calibrationPointDTO) throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException {
		var calibrationPoint = calibrationPointRepository
				.findById(calibrationPointId)
				.orElseThrow(() -> new EntityNotFoundException(CalibrationPoint.class));
		
		calibrationPoint = calibrationPointDtoMapper
				.updateToEntity(calibrationPoint, calibrationPointDTO);
		
		//checks if pointId exist in calibrationPoint aliases and database for this calibrationSet
		List<String> exists = calibrationPoint
				.getAliases()
				.contains(calibrationPoint.getPointId())? 
						new ArrayList<String>(Arrays.asList(calibrationPoint.getPointId())):
						new ArrayList<String>();
		
		//checks if one of the aliases can alredy be defined as pointId in this collection
		var existsInDatabase = calibrationPointRepository
				.existsByCalibrationSetIdAndPointId(calibrationPoint.getCalibrationSet().getId(), calibrationPoint.getAliases());
		exists.addAll(existsInDatabase);
		
		if (!exists.isEmpty())
			throw new UniquePropertyException(
					CalibrationPoint.class,
					List.of(CalibrationPoint.class.getDeclaredField("aliases"), CalibrationPoint.class.getDeclaredField("pointId")), 
					List.of(calibrationPoint.getId()),
					"Incorect values (already defined in the pointId property for this CalibrationSet)."
					);
		
		var result  = calibrationPointRepository.saveAndFlush(calibrationPoint);
		return projectionFactory
				.createProjection(SimpleCalibrationPointView.class, result);
	}
	
	@Override
	public CalibrationPointView getById(@NotNull String calibrationPointId) throws EntityNotFoundException {
		var calibrationPoint = calibrationPointRepository
				.findCalibrationPointById(calibrationPointId)
				.orElseThrow(() -> new EntityNotFoundException(CalibrationPoint.class));
		
		return calibrationPoint;
	}
	
	@Override
	public List<CalibrationPointView> getAllByCalibrationSetId(@NotNull String calibrationSetId) {
		var calibrationPoints = calibrationPointRepository
				.findAllByCalibrationSetId(calibrationSetId);
		
		return calibrationPoints;
	}
	
	@Override
	public void delete(@NotNull String calibrationPointId) {
		calibrationPointRepository.deleteById(calibrationPointId);
	}
	
	

}
