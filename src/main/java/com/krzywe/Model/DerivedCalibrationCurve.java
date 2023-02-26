package com.krzywe.Model;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("DERIVED_CALIBRATION_CURVE")
public class DerivedCalibrationCurve extends CalibrationCurveCalType /*AbstractPersistentObject*/ {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			foreignKey = @ForeignKey(
					name = "FK_DERIVEDCALIBRATIONCURVE_CALCULATEDCALIBRATIONCURVE_ID",
					foreignKeyDefinition = 
							"FOREIGN KEY (calculated_calibration_curve_id) "
							+ "REFERENCES calibration_curve_cal_type (id) "
							+ "ON DELETE CASCADE"
					)
			)
	private CalculatedCalibrationCurve calculatedCalibrationCurve;
	
	protected DerivedCalibrationCurve() {}

	public DerivedCalibrationCurve(
			@NotNull CalibrationCurveCalType calibrationCurveCalType,
			@NotNull CalculatedCalibrationCurve calculatedCalibrationCurve
			) {
		this.setCalculatedCalibrationCurve(calculatedCalibrationCurve);
	}

	public CalculatedCalibrationCurve getCalculatedCalibrationCurve() {
		return calculatedCalibrationCurve;
	}

	public void setCalculatedCalibrationCurve(CalculatedCalibrationCurve calculatedCalibrationCurve) {
		this.calculatedCalibrationCurve = calculatedCalibrationCurve;
	}

	@Override
	public Set<Sample> calculateSample(Set<Sample> samples) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * Clones an CalibrationCurve instance without id and all relations are null. CalculatedCalibrationCurve is null.
	 */
	@Override
	protected DerivedCalibrationCurve clone() throws CloneNotSupportedException {
		var clone = new DerivedCalibrationCurve();
		this.setId(null);
		return clone;
	}

}
