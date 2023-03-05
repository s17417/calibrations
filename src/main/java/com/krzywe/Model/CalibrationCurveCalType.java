package com.krzywe.Model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		discriminatorType = DiscriminatorType.STRING,
		length = 50,
		name = "calibrationType"
		)
public abstract class CalibrationCurveCalType extends AbstractPersistentObject implements Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(
			name = "id",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_CALIBRATIONCURVE_CALIBRATIONCURVECALTYPE_ID")
			)
	private CalibrationCurve calibrationCurve;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, insertable = false, updatable = false)
	private CalibrationType calibrationType;
	
	public CalibrationCurveCalType() {}

	public CalibrationCurve getCalibrationCurve() {
		return calibrationCurve;
	}

	public void setCalibrationCurve(CalibrationCurve calibrationCurve) {
		this.calibrationCurve = calibrationCurve;
	}
	
	public CalibrationType getCalibrationType() {
		return calibrationType;
	}

	protected void setCalibrationType(CalibrationType calibrationType) {
		this.calibrationType = calibrationType;
	}

	public abstract Set<Sample> calculateSample(Set<Sample> samples);

	@Override
	protected abstract CalibrationCurveCalType clone() throws CloneNotSupportedException;
	
	
	
	
}
