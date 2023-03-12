package com.krzywe.Controllers;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.krzywe.DTO.CalibrationSetDTO;
import com.krzywe.DTO.CalibrationSetView;
import com.krzywe.Service.ICalibrationSetService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/calibration_set")
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

}
