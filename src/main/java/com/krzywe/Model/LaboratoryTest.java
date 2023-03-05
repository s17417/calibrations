package com.krzywe.Model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_LABORATORY_TEST_NAME",columnNames = { "name" })})
public class LaboratoryTest extends AbstractPersistentObject {

	/**
	 * Class representing Laboratory test which groups analytes by method analytical method.
	 * Obligatory fields are name and material type.
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Size(min=3, max=100)
	@Column(nullable = false, length = 100)
	private String name;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private MaterialType materialType;
	
	@OneToMany(mappedBy = "laboratoryTest", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Method> method = new HashSet<>();

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

	public Set<Method> getMethod() {
		return method;
	}

	public <V extends Collection<Method>> void setMethod(V method) {
		this.method.clear();
		this.method.addAll(method);
	}
	
	public void addMethod(Method method) {
		method.setLaboratoryTest(this);
	}
	
	public void removeMethod(Method method) {
		if (this.method.contains(method))
			method.setLaboratoryTest(null);
	}
	
	
	
}
