package com.krzywe.Model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;


/**
 * Entity representing a calibration set, connected to each of defined {@link CalibrationPoint}.
 * Fields name and materialType must not be empty. Fields preparationDate and expirationDate are optional
 * @author tomek
 */
@Entity
public class CalibrationSet extends AbstractPersistentObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message="valid field cannot be empty")
	@Size(min=3, max=50, message = "valid length - 3 to 50 chars")
	@Column(unique = true, nullable=false, length = 50)
	private String name;
	
	@PastOrPresent(message = "valid date from past or present")
	private LocalDate preparationDate;
	
	@FutureOrPresent(message ="valid date from future or present")
	private LocalDate expirationDate;
	
	@NotNull(message = "valid field cannot be empty")
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

	public void setCalibrationPoints(Set<CalibrationPoint> calibrationPoints) {
		this.calibrationPoints.clear();
		this.calibrationPoints.addAll(calibrationPoints);
	}
	
	
	/**
	 * Adds {@link CalibrationPoint} instance to the set.
	 * @param calibrationPoint
	 * @throws NullPointerException in case of passed null as parameter.
	 */
	public void addCalibrationPoint(@NotNull CalibrationPoint calibrationPoint) throws NullPointerException {
		if (calibrationPoint==null) throw new NullPointerException("calibrationPoint must not be null");
		calibrationPoint.setCalibrationSet(this);
	}
	
	/**
	 * Removes {@link CalibrationPoint} instance attached to the set.
	 * @param calibrationPoint
	 * @throws NullPointerException
	 */
	public void removeCalibrationPoint(CalibrationPoint calibrationPoint) throws NullPointerException {
		if (calibrationPoint==null) throw new NullPointerException("calibrationPoint must not be null");
		if (this.calibrationPoints.contains(calibrationPoint))
			calibrationPoint.setCalibrationSet(null);
	}
	
	
	
}
