package com.krzywe.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	@NotBlank(message = "valid field can't be empty")
	@Size(min = 3, max = 32, message = "valid length - 3 to 32 chars")
	@Column(nullable = false, length = 32)
	private String pointId;

	@Size(max = 32, message = "valid collection size - up to 32 aliases")
	@ElementCollection(fetch = FetchType.LAZY)
	@UniqueElements(message = "must contain only unique elements")
	@OrderColumn(name = "ALIAS_ORDER")
	@JoinColumn(foreignKey = @ForeignKey(name="FK_CALIBRATION_POINT_ID"))
	@Column(length = 32)
	private List<@Size(min = 3,max = 32, message = "valid length - 3 to 32 chars") String> aliases = new ArrayList<String>();

	@NotNull(message = "valid field can't be empty")
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false,foreignKey = @ForeignKey(name="FK_CALIBRATION_SET_ID"))
	private CalibrationSet calibrationSet;

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
	
	
	
}
