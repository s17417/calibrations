package com.krzywe.DTO;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class TargetValueDTO {
	
	@Positive
	@Digits(fraction = 12, integer = 20)
	private BigDecimal targetValue;
	
	@Size(max = 50)
	private String units;

}
