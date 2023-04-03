package com.krzywe.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;

import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.SimpleCalibrationPointView;
import com.krzywe.DTO.mappers.CalibrationPointDtoMapper;
import com.krzywe.Model.CalibrationPoint;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;
import com.krzywe.Repository.CalibrationPointRepository;
import com.krzywe.Repository.CalibrationSetRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalibrationPointServiceImplTest {
	

	@Mock
	private CalibrationPointRepository pointRepository;	
	@Mock
	private CalibrationSetRepository setRepository;	
	@Mock
	private CalibrationPointDtoMapper mapper;		
	@Mock
	private ProjectionFactory projectionFactory;	
	@InjectMocks
	private CalibrationPointServiceImpl serviceImpl;
	
	CalibrationSet calibrationSet;	
	CalibrationPoint calibrationPoint;
	CalibrationPointDTO pointDTO;
	@Mock
	CalibrationPointView calibrationPointView;
	@Mock
	SimpleCalibrationPointView simpleCalibrationPointView;
	
	@BeforeEach
	public void beforeEach() {
		calibrationSet = new CalibrationSet();
		calibrationSet.setName("set_1");
		calibrationSet.setMaterialType(MaterialType.CSF);
		calibrationPoint = new CalibrationPoint();
		calibrationPoint.setPointId("point_1");
		calibrationPoint.setAliases(List.of("alias_1","alias_2","alias_3"));
		pointDTO = new CalibrationPointDTO();
		pointDTO.setPointId("point_1");
		pointDTO.setAliases(calibrationPoint.getAliases());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createTest() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		when(setRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationSet));
		when(mapper.calibrationPointDTOtoCalibrationPoint(pointDTO))
		.thenReturn(calibrationPoint);
		when(pointRepository.existsByCalibrationSetIdAndPointId(anyString(), anyCollection()))
		.thenReturn(List.of());
		when(pointRepository.saveAllAndFlush(anyList()))
		.thenReturn(List.of(calibrationPoint));
		when(projectionFactory.createProjection(ArgumentMatchers.isA(Class.class), any(CalibrationPoint.class)))
		.thenReturn(calibrationPointView);
		
		var result = serviceImpl.create(calibrationSet.getId(), List.of(pointDTO));
		assertThat(result)
		.isInstanceOf(List.class)
		.containsExactly(calibrationPointView);
		
	}
	
	@Test
	public void createTest_shouldThrowEntityNotFoundException() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		when(setRepository.findById(anyString()))
		.thenReturn(Optional.empty());
		
		var message = assertThrows(
				EntityNotFoundException.class,
				()->serviceImpl.create(calibrationSet.getId(), List.of(pointDTO))
				).getMessage();
		
		assertThat(message).contains("CalibrationSet");		
	}
	
	@Test
	public void createTest_shouldThrowUniquePropertyExceptionOnPropertyIdSameasAlias() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		calibrationPoint.setPointId("ALIAS_1");
		
		when(setRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationSet));
		when(mapper.calibrationPointDTOtoCalibrationPoint(pointDTO))
		.thenReturn(calibrationPoint);
		when(pointRepository.existsByCalibrationSetIdAndPointId(anyString(), anyCollection()))
		.thenReturn(List.of());
		
		var message = assertThrows(
				UniquePropertyException.class,
				()->serviceImpl.create(calibrationSet.getId(), List.of(pointDTO))
				).getMessage();
		
		assertThat(message)
		.contains("aliases")
		.contains("pointId")
		.contains("violates unique");		
	}
	
	@Test
	public void createTest_shouldThrowUniquePropertyExceptionOnDatabasePointId() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {
		
		when(setRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationSet));
		when(mapper.calibrationPointDTOtoCalibrationPoint(pointDTO))
		.thenReturn(calibrationPoint);
		when(pointRepository.existsByCalibrationSetIdAndPointId(anyString(), anyCollection()))
		.thenReturn(List.of("ALIAS_1"));
		
		var message = assertThrows(
				UniquePropertyException.class,
				()->serviceImpl.create(calibrationSet.getId(), List.of(pointDTO))
				).getMessage();
		
		assertThat(message)
		.contains("aliases")
		.contains("pointId")
		.contains("violates unique");		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateTest() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		calibrationPoint.setCalibrationSet(calibrationSet);
		
		when(pointRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationPoint));
		when(mapper.updateToEntity(any(CalibrationPoint.class),eq(pointDTO)))
		.thenReturn(calibrationPoint);
		when(pointRepository.existsByCalibrationSetIdAndPointId(anyString(), anyCollection()))
		.thenReturn(List.of());
		when(pointRepository.saveAndFlush(any(CalibrationPoint.class)))
		.thenReturn(calibrationPoint);
		when(projectionFactory.createProjection(ArgumentMatchers.isA(Class.class), any(CalibrationPoint.class)))
		.thenReturn(simpleCalibrationPointView);
		
		var result = serviceImpl.update(calibrationPoint.getId(), pointDTO);
		assertThat(result)
		.isEqualTo(simpleCalibrationPointView);	
	}
	
	@Test
	public void updateTest_shouldThrowEntityNotFoundException() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		
		when(pointRepository.findById(anyString()))
		.thenReturn(Optional.empty());
		
		var message = assertThrows(
				EntityNotFoundException.class, 
				() -> serviceImpl.update(calibrationPoint.getId(), pointDTO)
				).getMessage();
				
		assertThat(message)
		.contains("CalibrationPoint");			
	}
	
	@Test
	public void updateTest_shouldThrowUniquePropertyExceptionOnPropertyIdSameasAlias() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		calibrationPoint.setCalibrationSet(calibrationSet);
		calibrationPoint.setPointId("ALIAS_1");
		when(pointRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationPoint));
		when(mapper.updateToEntity(any(CalibrationPoint.class),eq(pointDTO)))
		.thenReturn(calibrationPoint);
		
		var message = assertThrows(
				UniquePropertyException.class, 
				() -> serviceImpl.update(calibrationPoint.getId(), pointDTO)
				).getMessage();
				
		assertThat(message)
		.contains("aliases")
		.contains("pointId")
		.contains("violates unique");				
	}
	
	@Test
	public void updateTest_shouldThrowUniquePropertyExceptionOnDatabasePointId() throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException {		
		calibrationPoint.setCalibrationSet(calibrationSet);
		when(pointRepository.findById(anyString()))
		.thenReturn(Optional.of(calibrationPoint));
		when(mapper.updateToEntity(any(CalibrationPoint.class),eq(pointDTO)))
		.thenReturn(calibrationPoint);
		when(pointRepository.existsByCalibrationSetIdAndPointId(anyString(), anyCollection()))
		.thenReturn(List.of("ALIAS_1"));
		
		var message = assertThrows(
				UniquePropertyException.class, 
				() -> serviceImpl.update(calibrationPoint.getId(), pointDTO)
				).getMessage();
				
		assertThat(message)
		.contains("aliases")
		.contains("pointId")
		.contains("violates unique");				
	}
	
	@Test
	public void getByIdTest() throws EntityNotFoundException {
		when(pointRepository.findCalibrationPointById(anyString()))
		.thenReturn(Optional.of(calibrationPointView));
		assertThat(serviceImpl.getById(calibrationPoint.getId()))
		.isInstanceOf(CalibrationPointView.class);
	}
	
	@Test
	public void getByIdTest_ShouldThrowEntityNotfound() throws EntityNotFoundException {
		when(pointRepository.findCalibrationPointById(anyString()))
		.thenReturn(Optional.empty());
		var message = assertThrows(
				EntityNotFoundException.class,
				() -> serviceImpl.getById(calibrationPoint.getId())
				)
		.getMessage();
		
		assertThat(message).contains("CalibrationPoint");
	}
}
