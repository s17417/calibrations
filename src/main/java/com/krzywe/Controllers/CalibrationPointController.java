package com.krzywe.Controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.krzywe.DTO.CalibrationPointDTO;
import com.krzywe.DTO.CalibrationPointView;
import com.krzywe.DTO.SimpleCalibrationPointView;
import com.krzywe.Service.ICalibrationPointService;
import com.krzywe.Utils.Exceptions.EntityNotFoundException;
import com.krzywe.Utils.Exceptions.UniquePropertyException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1")
@Validated
public class CalibrationPointController {
	
	private ICalibrationPointService service;

	public CalibrationPointController(ICalibrationPointService service) {
		this.service = service;
	}
	
	@PostMapping(
			value = "/calibration_set/{calibrationSetId}/calibration_point",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE	
			)
	public ResponseEntity<Collection<CalibrationPointView>> create(
			@PathVariable String calibrationSetId,
			@RequestBody @NotEmpty List<@Valid CalibrationPointDTO> calibrationPointDTO
			) throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException{
		var result = service.create(calibrationSetId, calibrationPointDTO);
		return ResponseEntity
				.created(ServletUriComponentsBuilder
						.fromCurrentRequest()
						.build()
						.toUri()
						)
				.body(result);		
	}
	
	@PutMapping(
			path ="/calibration_point/{calibrationPointId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<SimpleCalibrationPointView> update(
			@PathVariable String calibrationPointId,
			@Valid @RequestBody @NotNull CalibrationPointDTO calibrationPointDTO
			) throws NoSuchFieldException, SecurityException, EntityNotFoundException, UniquePropertyException{
		var result = service.update(calibrationPointId, calibrationPointDTO);
		return ResponseEntity
				.ok(result);
	}
	
	@GetMapping(
			path ="/calibration_point/{calibrationPointId}",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<CalibrationPointView> getById(@PathVariable String calibrationPointId)
			throws EntityNotFoundException{
		var result = service.getById(calibrationPointId);
		return ResponseEntity
				.ok(result);
	}
	
	@GetMapping(
			path ="/calibration_set/{calibrationSetId}",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<List<CalibrationPointView>> getAllByCalibrationSetId(@PathVariable String calibrationSetId){
		var result = service.getAllByCalibrationSetId(calibrationSetId);
		return ResponseEntity
				.ok(result);
		
	}
	
	

}
