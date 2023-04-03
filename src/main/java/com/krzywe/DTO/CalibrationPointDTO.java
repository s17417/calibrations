package com.krzywe.DTO;

import java.util.List;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CalibrationPointDTO {
	
	@NotBlank
	@Size(min = 3, max = 32)
	private String pointId;
	
	@Size(max = 32, message = "{aliases.Size.message}")
	@UniqueElements
	private List<@Size(min = 3,max = 32) String> aliases;

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
}
