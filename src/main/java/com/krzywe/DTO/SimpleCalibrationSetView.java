package com.krzywe.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.krzywe.Model.MaterialType;

public interface SimpleCalibrationSetView {
	
	public String getId();
	
	public String getName();
	
	public LocalDate getPreparationDate();
	
	public LocalDate getExpirationDate();
	
	public MaterialType getMaterialType();
	
	public LocalDateTime getCreatedDate();
}
