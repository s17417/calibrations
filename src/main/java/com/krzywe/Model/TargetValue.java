package com.krzywe.Model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class TargetValue extends AbstractPersistentObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Positive
	@Column(precision = 20, scale=12)
	@Digits(fraction = 12, integer = 20)
	private BigDecimal targetValue;
	
	@Size(max = 50)
	@Column(length = 50)
	private String Units;
	
	@NotNull(message = "valid field can't be empty")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CALIBRATIONPOINTID_TARGETVALUE"), nullable = false)
	private CalibrationPoint calibrationPoint;
	
	@NotNull(message = "valid field can't be empty")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ANALYTEID_TARGETVALUE"), nullable = false)
	private Analyte analyte;

	public BigDecimal getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	public String getUnits() {
		return Units;
	}

	public void setUnits(String units) {
		Units = units;
	}

	public Analyte getAnalyte() {
		return analyte;
	}

	public void setAnalyte(Analyte analyte) {
		this.analyte = analyte;
	}
	
	public CalibrationPoint getCalibrationPoint() {
		return calibrationPoint;
	}

	public void setCalibrationPoint(CalibrationPoint calibrationPoint) {
		if (this.calibrationPoint==null) {
			if (calibrationPoint==null)
				return;
			calibrationPoint.getTargetValues().add(this);
			this.calibrationPoint=calibrationPoint;
		}
		if (this.calibrationPoint.getTargetValues().remove(this)) {
			this.calibrationPoint = calibrationPoint;
			if (this.calibrationPoint != null)
				this.calibrationPoint.getTargetValues().add(this);
		}
	}
	
	

}
