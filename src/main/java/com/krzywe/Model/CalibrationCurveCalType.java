package com.krzywe.Model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

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
