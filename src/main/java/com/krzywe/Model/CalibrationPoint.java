package com.krzywe.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.core.annotation.Order;

@Entity
public class CalibrationPoint extends AbstractPersistentObject {
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "valid field cannot be empty")
	@Size(min = 3, max = 32, message = "valid length - 3 to 32 chars")
	@Column(nullable = false, unique = false, length = 32)
	private String pointId;

	@Size(max = 32, message = "valid collection size - up to 32 aliases")
	@ElementCollection(fetch = FetchType.LAZY)
	@OrderColumn(name = "ALIAS_ORDER")
	@Column(length = 32)
	private List<@Size(min = 3,max = 32, message = "valid length - 3 to 32 chars") String> aliases = new ArrayList<String>();

	@NotNull(message = "valid field cannot be empty")
	@ManyToOne(optional = false)
	@JoinColumn(nullable = false,foreignKey = @ForeignKey(name="FK_CALIBRATION_SET"))
	private CalibrationSet calibrationSet;

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

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
		if (this.calibrationSet.getCalibrationPoints().remove(this))
			this.calibrationSet=calibrationSet;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases.clear();	
		this.aliases.addAll(
				aliases
				.stream()
				.distinct()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				);
	}
	
	public void addAlias(String alias) {
		if (!this.aliases.contains(alias.toUpperCase()))
			this.aliases.add(alias.toUpperCase());
	}
	
	public void removeAlias(String alias) {
		this.aliases.remove(alias.toUpperCase());
	}
	
	
	
}
