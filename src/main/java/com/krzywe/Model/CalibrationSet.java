package com.krzywe.Model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;


/**
 * Entity representing a calibration set, connected to each of defined {@link CalibrationPoint}.
 * Fields name and materialType must not be empty. Fields preparationDate and expirationDate are optional
 * @author tomek
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_CALIBRATION_SET_NAME",columnNames = { "name" })})
public class CalibrationSet extends AbstractPersistentObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@OneToMany(orphanRemoval = true,fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE},mappedBy = "calibrationSet")
	private Set<CalibrationPoint> calibrationPoints = new HashSet<CalibrationPoint>();
	
	/**
	 * returns name of CalibrationSet. Attribute {@link NotBlank}, {@link Size} 3-50 chars. 
	 * @return {@link String} 
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The date of CalibrationSet preparation.
	 * @return {@link LocalDate}
	 */
	public LocalDate getPreparationDate() {
		return preparationDate;
	}

	public void setPreparationDate(LocalDate preparationDate) {
		this.preparationDate = preparationDate;
	}

	/**
	 * Expiration date of CalibrationSet. Optional attribute.
	 * @return {@link LocalDate}
	 */
	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Defined biological material type of this control set. Attribute {@link NotNull}
	 * @return  {@link MaterialType}
	 */
	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	/**
	 * Returns list of attached controlPoints. Optional attribute.
	 * @return {@link Set}<{@link CalibrationPoint}>
	 */
	public Set<CalibrationPoint> getCalibrationPoints() {
		return calibrationPoints;
	}

	public <V extends Collection<CalibrationPoint>> void setCalibrationPoints(V calibrationPoints) {
		this.calibrationPoints.clear();
		this.calibrationPoints.addAll(calibrationPoints);
	}
	
	
	/**
	 * Adds {@link CalibrationPoint} instance to the set.
	 * @param calibrationPoint
	 * @throws NullPointerException in case of passed null as parameter.
	 */
	public void addCalibrationPoint(@NotNull CalibrationPoint calibrationPoint) throws NullPointerException {
		if (calibrationPoint!=null)
			calibrationPoint.setCalibrationSet(this);
	}
	
	/**
	 * Removes {@link CalibrationPoint} instance attached to the set.
	 * @param calibrationPoint
	 * @throws NullPointerException
	 */
	public void removeCalibrationPoint(CalibrationPoint calibrationPoint) throws NullPointerException {
		if (calibrationPoint!=null && this.calibrationPoints.contains(calibrationPoint))
			calibrationPoint.setCalibrationSet(null);
	}
	
	
	
}
