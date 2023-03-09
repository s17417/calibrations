package com.krzywe.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.projection.ProjectionFactory;
import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.DTO.mappers.CalibrationSetDtoMapper;
import com.krzywe.Model.CalibrationSet;
import com.krzywe.Model.MaterialType;
import com.krzywe.Repository.CalibrationSetRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Specifications.ICalibrationSetSpec;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalibrationSetServiceImplTest {
	
	@Mock
	private CalibrationSetRepository repository; 
	
	@Mock
	private CalibrationSetDtoMapper calibrationSetDtoMapper; 
	
	@Mock
	private ProjectionFactory projectionFactory;
	
	@Mock
	private ICalibrationSetSpec specification;
	
	@InjectMocks
	private CalibrationSetServiceImpl serviceImpl;
	
	private CalibrationSetDTO calibrationSetDTO;
	
	private CalibrationSet calibrationSet;
	
	@Mock
	private CalibrationSetView calibrationSetView;
	
	@Mock
	private Page<SimpleCalibrationSetView> page;
	
	@BeforeEach
	public void beforeEach() {
		calibrationSetDTO = new CalibrationSetDTO();
		calibrationSet = new CalibrationSet();
		calibrationSetDTO.setName("name");
		calibrationSetDTO.setMaterialType(MaterialType.DBS);
		calibrationSetDTO.setPreparationDate(LocalDate.of(2020, 10, 1));
		calibrationSetDTO.setExpirationDate(LocalDate.of(2021, 10, 1));
		calibrationSet.setName("name");
		calibrationSet.setMaterialType(MaterialType.DBS);
		calibrationSet.setPreparationDate(LocalDate.of(2020, 10, 1));
		calibrationSet.setExpirationDate(LocalDate.of(2021, 10, 1));	
	}
	
	@Test
	public void CalibrationSetServiceImplTest_create() {
		when(calibrationSetDtoMapper.CalibrationSetDtoToCalibrationSet(calibrationSetDTO))
		.thenReturn(calibrationSet);
		
		when(repository.save(calibrationSet))
		.thenReturn(calibrationSet);
		
		when(projectionFactory.createProjection(CalibrationSetView.class, calibrationSet))
		.thenReturn(calibrationSetView);
		
		assertThat(serviceImpl.create(calibrationSetDTO))
		.isNotNull()
		.isEqualTo(calibrationSetView);
	}
	
	@Test
	public void CalibrationSetServiceImplTestUpdate_ReturnObject() throws EntityNotFoundException {
		var id = calibrationSet.getId();
		when(repository.findById(id))
		.thenReturn(Optional.of(calibrationSet));
		
		when(calibrationSetDtoMapper.updateToEntity(calibrationSet, calibrationSetDTO))
		.thenReturn(calibrationSet);
		
		when(repository.save(calibrationSet))
		.thenReturn(calibrationSet);
		when(projectionFactory.createProjection(CalibrationSetView.class, calibrationSet))
		.thenReturn(calibrationSetView);
		
		
		assertThat(serviceImpl.update(calibrationSetDTO, id))
		.isNotNull()
		.isEqualTo(calibrationSetView);		
	}
	
	@Test
	public void CalibrationSetServiceImplTestUpdate_NotFoundShouldThrowException() throws EntityNotFoundException {
		var id = UUID.randomUUID().toString();
		when(repository.findById(id))
		.thenReturn(Optional.empty());
		
		assertThrows(
				EntityNotFoundException.class,
				()-> serviceImpl.update(calibrationSetDTO, id),
				"Entity of type: "+CalibrationSet.class.getSimpleName()+" not found."
				);	
	}
	
	@Test
	public void CalibrationSetServiceImplTestFindById_ReturnObject() throws EntityNotFoundException {
		var id = calibrationSet.getId();
		when(repository.findByIdAsView(id))
		.thenReturn(Optional.of(calibrationSetView));
		
		assertThat(serviceImpl.findById(id))
		.isNotNull()
		.isEqualTo(calibrationSetView);		
	}
	
	@Test
	public void CalibrationSetServiceImplTestFindById_NotFoundShouldThrowException() throws EntityNotFoundException {
		var id = calibrationSet.getId();
		when(repository.findByIdAsView(id))
		.thenReturn(Optional.empty());
		
		assertThrows(
				EntityNotFoundException.class,
				()-> serviceImpl.findById(id),
				"Entity of type: "+CalibrationSet.class.getSimpleName()+" not found."
				);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void CalibrationSetServiceImplTestFindAll_ReturnObject() {
		when(repository.findBy(any(Specification.class), any(Function.class)))
		.thenReturn(page);
		when(specification.getAllSpecificationsChain(
				anyString(),
				any(MaterialType.class),
				any(LocalDateTime.class), 
				any(LocalDateTime.class),
				any(LocalDate.class),
				any(LocalDate.class)
				)
			)
		.thenReturn(Specification.where(null));
		
		assertThat(
				serviceImpl.findAll(
						"name",
						MaterialType.CSF,
						LocalDate.now().minusDays(1L),
						LocalDate.now(),
						LocalDateTime.now().minusDays(1L),
						LocalDateTime.now().minusDays(1L),
						0,
						10,
						Direction.ASC,
						"name"
						)
				)
		.isNotNull();
	}
	

}
