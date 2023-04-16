package com.krzywe.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;

import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.DTO.mappers.AnalyteDtoMapper;
import com.krzywe.Model.Analyte;
import com.krzywe.Repository.AnalyteRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;
import com.krzywe.Utils.Specifications.IAnalyteSpec;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AnalyteServiceTest {
	
	@Mock
	private AnalyteRepository repository;
	
	@Mock
	private ProjectionFactory factory;

	@Mock
	private AnalyteDtoMapper mapper;
	
	@Mock
	private IAnalyteSpec spec;
	
	@InjectMocks
	private AnalyteServiceImpl serviceImpl;
	
	private Analyte analyte;
	private AnalyteDTO dto;
	@Mock
	private AnalyteNamesView namesView;
	@Mock
	private AnalyteView view;
	@Mock
	private AnalyteWithAliasesView aliasesView;
	
	@BeforeEach
	public void beforeEach() {
		analyte = new Analyte();
		analyte.setName("analyte_1");
		analyte.setAliases(List.of("alias_1","alias_2"));
		
		dto = new AnalyteDTO();
		dto.setName(analyte.getName());
		dto.setAliases(analyte.getAliases());
	}
	
	@Test
	public void createTest() {
		when(mapper.analyeDTOtoAnalyte(any(AnalyteDTO.class)))
		.thenReturn(analyte);
		when(repository.aliasExists(anyList()))
		.thenReturn(List.of());
		when(repository.save(any(Analyte.class)))
		.thenReturn(analyte);
		when(factory.createProjection(isA(Class.class),any(Analyte.class)))
		.thenReturn(aliasesView);
		
		var result = assertDoesNotThrow(() -> serviceImpl.create(dto));
		assertThat(result)
		.isNotNull()
		.isInstanceOf(AnalyteWithAliasesView.class);
		
	}
	
	@Test
	public void createTest_shouldThrowUniquePropertyException() {
		when(mapper.analyeDTOtoAnalyte(any(AnalyteDTO.class)))
		.thenReturn(analyte);
		when(repository.aliasExists(anyList()))
		.thenReturn(List.of("alias_1"));
		
		var result = assertThrows(UniquePropertyException.class,() -> serviceImpl.create(dto));
		assertThat(result.getMessage())
		.isNotNull()
		.contains("Analyte","violates unique field constraint on property");
	}
	
	@Test
	public void updateTest() {
		when(repository.findById(anyString()))
		.thenReturn(Optional.of(analyte));
		when(mapper.updateToEntity(any(Analyte.class), any(AnalyteDTO.class)))
		.thenReturn(analyte);
		when(repository.aliasExists(anyList()))
		.thenReturn(List.of());
		when(repository.save(any(Analyte.class)))
		.thenReturn(analyte);
		when(factory.createProjection(isA(Class.class),any(Analyte.class)))
		.thenReturn(aliasesView);
		
		var result = assertDoesNotThrow(() -> serviceImpl.update(analyte.getId(), dto));
		assertThat(result)
		.isNotNull()
		.isInstanceOf(AnalyteWithAliasesView.class);
	}
	
	@Test
	public void updateTest_shouldThrowEntityNotFoundException() {
		when(repository.findById(anyString()))
		.thenReturn(Optional.empty());
		
		var result = assertThrows(
				EntityNotFoundException.class,
				() -> serviceImpl.update(analyte.getId(), dto)
				);
		assertThat(result.getMessage())
		.isNotNull()
		.contains("Analyte","not found");
	}
	
	@Test
	public void updateTest_shouldUniquePropertyException() {
		when(repository.findById(anyString()))
		.thenReturn(Optional.of(analyte));
		when(mapper.updateToEntity(any(Analyte.class), any(AnalyteDTO.class)))
		.thenReturn(analyte);
		when(repository.aliasExists(anyList()))
		.thenReturn(List.of("alias_1"));
		
		var result = assertThrows(
				UniquePropertyException.class,
				() -> serviceImpl.update(analyte.getId(), dto)
				);
		assertThat(result.getMessage())
		.isNotNull()
		.contains("Analyte","violates unique field constraint on property");
	}
	
	
	@Test
	public void getByIdTest() {
		when(repository.findAnalyteById(anyString()))
		.thenReturn(Optional.of(aliasesView));
		
		var result = assertDoesNotThrow(() -> serviceImpl.getById(analyte.getId()));
		assertThat(result)
		.isNotNull()
		.isInstanceOf(AnalyteWithAliasesView.class);
	}
	
	@Test
	public void getByIdTest_shouldThrowEntityNotFoundException() {
		when(repository.findAnalyteById(anyString()))
		.thenReturn(Optional.empty());
		
		var result = assertThrows(
				EntityNotFoundException.class,
				() -> serviceImpl.getById(analyte.getId())
				);
		assertThat(result.getMessage())
		.isNotNull()
		.contains("Analyte","not found");
	}
	
}
