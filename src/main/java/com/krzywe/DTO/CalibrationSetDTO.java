package com.krzywe.DTO;

import java.time.LocalDate;

import com.krzywe.Model.MaterialType;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class CalibrationSetDTO {
	
	@NotBlank
	@Size(min=3, max=50)
	@Column(nullable=false, length = 50)
	private String name;
	
	@PastOrPresent
	private LocalDate preparationDate;
	
	@FutureOrPresent
	private LocalDate expirationDate;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MaterialType materialType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getPreparationDate() {
		return preparationDate;
	}

	public void setPreparationDate(LocalDate preparationDate) {
		this.preparationDate = preparationDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
}
