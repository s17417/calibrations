package com.krzywe.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;

import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.DTO.mappers.AnalyteDtoMapper;
import com.krzywe.Model.Analyte;
import com.krzywe.Model.Analyte_;
import com.krzywe.Repository.AnalyteRepository;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;
import com.krzywe.Utils.Specifications.IAnalyteSpec;

@Service
public class AnalyteServiceImpl implements IAnalyteService {

	private AnalyteRepository repository;
	
	private ProjectionFactory factory;
	
	private AnalyteDtoMapper mapper;

	private IAnalyteSpec specDef;

	public AnalyteServiceImpl(AnalyteRepository repository, ProjectionFactory factory, AnalyteDtoMapper mapper,
			IAnalyteSpec specDef) {
		super();
		this.repository = repository;
		this.factory = factory;
		this.mapper = mapper;
		this.specDef = specDef;
	}

	@Override
	public AnalyteWithAliasesView create(AnalyteDTO analyteDTO) throws NoSuchFieldException, SecurityException, UniquePropertyException {
		var analyte = mapper.analyeDTOtoAnalyte(analyteDTO);
		var aliasExists = repository.aliasExists(analyte.getAliases());
		Analyte result = null;
		
		if (aliasExists.isEmpty() && !analyte.getAliases().contains(analyte.getName()))
			result = repository.save(analyte);
		else throw new UniquePropertyException(
				Analyte.class,
				List.of(Analyte.class.getDeclaredField(Analyte_.ALIASES)),
				aliasExists
				);
		
		return factory
				.createProjection(AnalyteWithAliasesView.class, result);	
	}
	
	@Override
	public AnalyteWithAliasesView update(String id, AnalyteDTO analyteDTO) throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException {
		var analyte = repository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(Analyte.class));
		Analyte updatedEntity = mapper.updateToEntity(analyte, analyteDTO);
		var aliasExists = repository.aliasExists(updatedEntity.getAliases());
		
		if (aliasExists.isEmpty() && !analyte.getAliases().contains(analyte.getName()))
			updatedEntity = repository.save(updatedEntity);
		else throw new UniquePropertyException(
				Analyte.class,
				List.of(Analyte.class.getDeclaredField(Analyte_.ALIASES)),
				aliasExists
				);
		
		return factory.createProjection(AnalyteWithAliasesView.class, updatedEntity);
	}
	
	@Override
	public List<AnalyteNamesView> getAllAnalyteNames(){
		return repository.findAnalytesNames();
	}
	
	@Override
	public AnalyteWithAliasesView getById(String id) throws EntityNotFoundException {
		return repository
				.findAnalyteById(id)
				.orElseThrow(() -> 
					new EntityNotFoundException(Analyte.class)
					);
	}
	
	@Override
	public Page<AnalyteView> getAll(
			String name,
			String alias,
			int pageNumber,
			int pageSize,
			Direction sortDirection,
			String sortAttribute) {
		
		return repository.findBy(
				specDef.getAllSpecificationsChain(
						name,
						alias
						),
				obj -> obj.as(AnalyteView.class)
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
