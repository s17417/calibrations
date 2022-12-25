package com.krzywe.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;


/**
 * Calibration point, groups an analytes by their exact target concentration, is part of CalibrationSet
 *  Attribute {@link CalibrationSet} is {@link NotNull}
 * @author tomek
 *
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_CALIBRATION_POINT_POINTID",columnNames = { "pointId" })})
public class CalibrationPoint extends AbstractPersistentObject {
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 3, max = 32)
	@Column(nullable = false, length = 32)
	private String pointId;

	@Size(max = 32, message = "{aliases.Size.message}")
	@ElementCollection(fetch = FetchType.LAZY)
	@UniqueElements
	@OrderColumn(name = "ALIAS_ORDER")
	@JoinColumn(foreignKey = @ForeignKey(name="FK_CALIBRATION_POINT_ID"))
	@Column(length = 32)
	private List<@Size(min = 3,max = 32) String> aliases = new ArrayList<String>();

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false,foreignKey = @ForeignKey(name="FK_CALIBRATION_SET_ID"))
	private CalibrationSet calibrationSet;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "calibrationPoint", orphanRemoval = true)
	private Set<TargetValue> targetValues = new HashSet<>();

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
	/**
	 *  Return CalibrationSet. This Attribute is {@link NotNull}.
	 * @param calibrationSet
	 */
	public CalibrationSet getCalibrationSet() {
		return calibrationSet;
	}

	
	public void setCalibrationSet(CalibrationSet calibrationSet) {
		if (this.calibrationSet == null) {
			if (calibrationSet == null) return;
				calibrationSet.getCalibrationPoints().add(this);
				this.calibrationSet=calibrationSet;
				return;
		}
		if (this.calibrationSet.getCalibrationPoints().remove(this)) {
			this.calibrationSet=calibrationSet;
			if (this.calibrationSet!=null)
				this.calibrationSet.getCalibrationPoints().add(this);
		}
	}

	/**
	 * Return list of aliases stored in upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases.clear();	
		this.aliases.addAll(
				aliases
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				);
	}
	
	/**
	 * Adds alias to aliases list. Alias is transformed to upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * @param {@link String} alias
	 */
	public void addAlias(String alias) {
			this.aliases.add(alias.toUpperCase());
	}
	
	/**
	 * Removes alias to aliases list. Alias is transformed to upper case. List {@link Size} up to 32 and alias {@link Size} 3 - 32.
	 * @param {@link String} alias
	 */
	public void removeAlias(String alias) {
		this.aliases.remove(alias.toUpperCase());
	}

	public Set<TargetValue> getTargetValues() {
		return targetValues;
	}

	public <V extends Collection<TargetValue>> void setTargetValues(V targetValues) {
		this.targetValues.clear();
		this.targetValues.addAll(targetValues);
	}
	
	public void addTargetValue(TargetValue targetValue) {
		if (targetValue!=null)
			targetValue.setCalibrationPoint(this);
	}
	
	public void removeTargetValue(TargetValue targetValue) {
		if (targetValue!=null && this.targetValues.contains(targetValue)) {
			targetValue.setCalibrationPoint(null);
		}
	}

	
	
	
	
	
	
}
