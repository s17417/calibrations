package com.krzywe.Controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.krzywe.DTO.AnalyteDTO;
import com.krzywe.DTO.AnalyteNamesView;
import com.krzywe.DTO.AnalyteView;
import com.krzywe.DTO.AnalyteWithAliasesView;
import com.krzywe.Service.IAnalyteService;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/analyte")
@Validated
public class AnalyteController {
	
	private IAnalyteService service;

	public AnalyteController(IAnalyteService service) {
		this.service = service;
	}
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<AnalyteWithAliasesView> create(
			@Valid @RequestBody @NotNull AnalyteDTO analyteDTO
			) throws NoSuchFieldException, SecurityException, UniquePropertyException {
		var result = service.create(analyteDTO);
		
		return ResponseEntity
				 .created(ServletUriComponentsBuilder
							.fromCurrentRequest()
							.build()
							.toUri()
							)
				 .body(result);	
	}
	
	@PutMapping(
			path ="/{analyteId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<AnalyteWithAliasesView> update(
			@PathVariable String analyteId,
			@Valid @RequestBody @NotNull AnalyteDTO analyteDTO
			) throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException{
		var result = service.update(analyteId, analyteDTO);
		
		return ResponseEntity
				.ok(result);
	}
	
	@GetMapping(
			path = "/{analyteId}",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<AnalyteWithAliasesView> getById(@PathVariable String analyteId) throws EntityNotFoundException{
		var result = service.getById(analyteId);
		
		return ResponseEntity
				.ok(result);
	}
	
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Page<AnalyteView>> getById(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String alias,
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "25") int pageSize,
			@RequestParam(defaultValue = "DESC") Direction sortDirection,
			@RequestParam(defaultValue = "createdDate") String sortAttribute		
			) throws EntityNotFoundException{
		var result = service.getAll(
				name,
				alias,
				pageNumber,
				pageSize,
				sortDirection,
				sortAttribute
				);
		
		return ResponseEntity
				.ok(result);
	}
	
	@GetMapping(
			path = "/names",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<List<AnalyteNamesView>> getAllNames(){
		var result = service.getAllAnalyteNames();
		
		return ResponseEntity
				.ok(result);
	}
	
	
	
}
