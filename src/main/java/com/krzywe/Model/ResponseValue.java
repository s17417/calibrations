package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import com.krzywe.Utils.Exceptions.CalculationResponseValueStrategyNullPointerException;

@Entity
public class ResponseValue extends AbstractPersistentObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@PositiveOrZero
	@Digits(fraction = 12, integer = 20)
	@Column(precision = 32, scale=12, nullable = false)
	private BigDecimal responseValue = BigDecimal.ZERO;
	
	@Positive
	@Digits(fraction = 12, integer = 20)
	@Column(precision = 32, scale=12)
	private BigDecimal internalStdResponseValue;
	
	@Positive
	@Digits(fraction = 12, integer = 20)
	@Column(precision = 32, scale=12)
	private BigDecimal internalStdConcentration;/*=BigDecimal.ONE*/;
	
	@Transient
	private Optional<ICalculationResponseValueStrategy> calculationResponseValueStrategy = ResponseValueCalculationFactoryImpl.getFactory()
			.createCalculationStrategy("EXTERNAL_STD");
	
	private Boolean isActive=true;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_RESPONSEVALUE_CALCULATEDCALIBRATIONCURVE_ID")
	)
	private CalculatedCalibrationCurve calculatedCalibrationCurve;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public CalculatedCalibrationCurve getCalculatedCalibrationCurve() {
		return calculatedCalibrationCurve;
	}
	
	public BigDecimal getResponseCalculationValue() throws CalculationResponseValueStrategyNullPointerException {
		return calculationResponseValueStrategy
				.orElseThrow(() -> new CalculationResponseValueStrategyNullPointerException("Calculation Strategy must not be null"))
				.getResponseCalculationValue(responseValue, internalStdResponseValue);	
	}
	
	public BigDecimal getTargetConcentrationValue() throws CalculationResponseValueStrategyNullPointerException {
		return calculationResponseValueStrategy
				.orElseThrow(() -> new CalculationResponseValueStrategyNullPointerException("Calculation Strategy must not be null"))
				.getTargetConcentrationValue(targetValue.getTargetValue(), internalStdConcentration);	
	}

	public Optional<ICalculationResponseValueStrategy> getCalculationResponseValueStrategy() {
		return calculationResponseValueStrategy;
	}

	protected void setCalculationResponseValueStrategy(ICalculationResponseValueStrategy calculationResponseValueStrategy) {
		this.calculationResponseValueStrategy = Optional.ofNullable(calculationResponseValueStrategy);
	}

	public BigDecimal getInternalStdResponseValue() {
		return internalStdResponseValue;
	}

	public void setInternalStdResponseValue(BigDecimal internalStdResponseValue) {
		this.internalStdResponseValue = internalStdResponseValue;
	}

	public BigDecimal getInternalStdConcentration() {
		return internalStdConcentration;
	}

	public void setInternalStdConcentration(BigDecimal internalStdConcentration) {
		this.internalStdConcentration = internalStdConcentration;
	}

	public void setCalculatedCalibrationCurve(CalculatedCalibrationCurve calculatedCalibrationCurve) {
		if (this.calculatedCalibrationCurve == null) {
			if (calculatedCalibrationCurve == null) return;
			calculatedCalibrationCurve.getResponseValues().add(this);
				this.calculatedCalibrationCurve=calculatedCalibrationCurve;
				return;
		}
		if (this.calculatedCalibrationCurve.getResponseValues().remove(this)) {
			this.calculatedCalibrationCurve=calculatedCalibrationCurve;
			if (this.calculatedCalibrationCurve!=null)
				this.calculatedCalibrationCurve.getResponseValues().add(this);
		}
	}
}
