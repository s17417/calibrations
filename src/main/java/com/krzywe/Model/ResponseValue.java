package com.krzywe.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class ResponseValue extends AbstractPersistentObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal responseValue;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime modifiedDate;
	
	private Boolean isActive=true;
	
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private CalibrationCurve calibrationCurve;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_RESPONSEVALUE_TARGETVALUE_ID"),
			columnDefinition = 
					"FOREIGN KEY (targetValue_id)"
					+ "REFERENCES TargetValue (Id)"
					+ "ON DELETE CASCADE"
			)
	private TargetValue targetValue;

	public BigDecimal getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(BigDecimal responseValue) {
		this.responseValue = responseValue;
	}

	public TargetValue getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(TargetValue targetValue) {
		this.targetValue = targetValue;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CalibrationCurve getCalibrationCurve() {
		return calibrationCurve;
	}

	public void setCalibrationCurve(CalibrationCurve calibrationCurve) {
		if (this.calibrationCurve == null) {
			if (calibrationCurve == null) return;
			calibrationCurve.getResponseValues().add(this);
				this.calibrationCurve=calibrationCurve;
				return;
		}
		if (this.calibrationCurve.getResponseValues().remove(this)) {
			this.calibrationCurve=calibrationCurve;
			if (this.calibrationCurve!=null)
				this.calibrationCurve.getResponseValues().add(this);
		}
	}
}
