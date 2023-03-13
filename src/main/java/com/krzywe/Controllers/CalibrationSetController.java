package com.krzywe.Controllers;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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

import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.DTO.SimpleCalibrationSetView;
import com.krzywe.Model.MaterialType;
import com.krzywe.Service.ICalibrationSetService;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/calibration_set")
@Validated
public class CalibrationSetController {
	
	private ICalibrationSetService service;

	public CalibrationSetController(ICalibrationSetService service) {
		this.service = service;
	}
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE			
			)
	public ResponseEntity<CalibrationSetView> create(@Valid @RequestBody @NotNull CalibrationSetDTO calibrationSetDTO){
		var result = service.create(calibrationSetDTO);
		return ResponseEntity
				.created(ServletUriComponentsBuilder
						.fromCurrentRequest()
						.path("/{id}")
						.build(result.getId())
						)
				.body(result);
	}
	
	@PutMapping(
			path ="/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<CalibrationSetView> update(@Valid @RequestBody @NotNull CalibrationSetDTO calibrationSetDTO, @PathVariable String id) throws EntityNotFoundException{
		var result = service.update(calibrationSetDTO, id);
		return ResponseEntity
				.ok()
				.body(result);
	}
	
	@GetMapping(
			path ="/{id}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<CalibrationSetView> findById(@PathVariable String id) throws EntityNotFoundException{
		return ResponseEntity
				.ok(service.findById(id));
	}
	
	@GetMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Page<SimpleCalibrationSetView>> findAll(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) MaterialType materialType,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate preparationDateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate preparationDateTo,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime createdDateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime createdDateTo,
			@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "25") int pageSize,
			@RequestParam(defaultValue = "DESC") Direction sortDirection,
			@RequestParam(defaultValue = "createdDate") String sortAttribute
			){
		var result = service.findAll(
				name,
				materialType,
				preparationDateFrom,
				preparationDateTo,
				createdDateFrom,
				createdDateTo,
				pageNumber,
				pageSize,
				sortDirection,
				sortAttribute
				);
		
		return ResponseEntity
				.ok(result);
	}

}
