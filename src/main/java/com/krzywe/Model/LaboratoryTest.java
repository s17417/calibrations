package com.krzywe.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_LABORATORY_TEST_NAME",columnNames = { "name" })})
public class LaboratoryTest extends AbstractPersistentObject {

	/**
	 * Class representing Laboratory test which groups analytes by method analytical method.
	 * Obligatory fields are name and material type.
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "valid field can't be empty")
	@Size(min=3, max=100, message = "valid length - 3 to 100 chars")
	@Column(nullable = false, length = 100)
	private String name;
	
	@NotNull(message = "valid field can't be empty")
	@Enumerated(EnumType.STRING)
	private MaterialType materialType;

	/**
	 * Returns laboratory test name. Applied constraints {@link NotBlank}, {@link Size} 3 - 100 and unique
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Defined biological material type of this laboratory test. Attribute {@link NotNull}
	 * @return  {@link MaterialType}
	 */
	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
	
}
