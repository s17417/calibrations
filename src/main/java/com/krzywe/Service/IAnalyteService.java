package com.krzywe.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

public interface IAnalyteService {

	AnalyteWithAliasesView create(AnalyteDTO analyteDTO)
			throws NoSuchFieldException, SecurityException, UniquePropertyException;

	AnalyteWithAliasesView update(String id, AnalyteDTO analyteDTO)
			throws EntityNotFoundException, NoSuchFieldException, SecurityException, UniquePropertyException;

	List<AnalyteNamesView> getAllAnalyteNames();

	Page<AnalyteView> getAll(String name, String alias, int pageNumber, int pageSize, Direction sortDirection,
			String sortAttribute);

	AnalyteWithAliasesView getById(String id) throws EntityNotFoundException;

}