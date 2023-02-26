package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.krzywe.Utils.Exceptions.FunctionStrategyException;


@Entity
@DiscriminatorValue("CALCULATED_CALIBRATION_CURVE")
public class CalculatedCalibrationCurve extends CalibrationCurveCalType /*AbstractPersistentObject*/ {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Digits(fraction = 4, integer = 1)
	@Column(precision = 5, scale=4)
	@PositiveOrZero
	@Max(1)
	private BigDecimal coefficientOfDetermination;
	
	@OrderColumn
	@ElementCollection
	@Column(precision = 32, scale=12)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_PARAMETERS_CALCULATEDCALIBRATIONCURVE_ID"))
	private List<@Digits(fraction = 12, integer = 20) BigDecimal> parameters = new ArrayList<>();
	
	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.ALL}
			)
	@JoinColumn(
			foreignKey = @ForeignKey(
					name = "FK_STANDARDCALIBRATIONCURVETYPE_RESPONSEVALUE_ID"
					)
			)
	private Set<ResponseValue> responseValues = new HashSet<>();
	
	@NotNull
	@Convert(converter = CalculateEquationConverter.class)
	@Column(nullable = false)
	private IFunctionStrategy iCalculateEquation = new LinearFunctionStrategy();
	
	@Enumerated(EnumType.STRING)
	private WEIGHT weight = WEIGHT.UNWEIGHTED;
	
	@NotNull
	@Convert(converter = CalculationResponseValueStrategyConverter.class)
	@Column(nullable = false)
	private ICalculationResponseValueStrategy calculationResponseValueStrategy = ResponseValueCalculationFactoryImpl
		.getFactory()
		.createCalculationStrategy("EXTERNAL_STD").get();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InternalStandard internalStandard;
	
	public CalculatedCalibrationCurve() {	
	}
	
	public IFunctionStrategy getiCalculateEquation() {
		return iCalculateEquation;
	}

	public void setiCalculateEquation(IFunctionStrategy iCalculateEquation) {
		this.iCalculateEquation = iCalculateEquation;
	}

	public List<BigDecimal> getParameters() {
		return parameters;
	}

	public void setParameters(List<BigDecimal> parameters) throws FunctionStrategyException {
		if (iCalculateEquation==null) throw new FunctionStrategyException("Function Strategy must not be null");
		this.getParameters().clear();
		this.getParameters().addAll(iCalculateEquation.setParameters(parameters));
	}

	public Optional<BigDecimal> getCoefficientOfDetermination() {
		return Optional.ofNullable(coefficientOfDetermination);
	}
	
	public void setCoefficientOfDetermination(BigDecimal coefficientOfDetermination) {
		this.coefficientOfDetermination = coefficientOfDetermination;
	}

	public Optional<String> getEquation() throws FunctionStrategyException {
		if (iCalculateEquation==null) throw new FunctionStrategyException("Function Strategy must not be null");
		return iCalculateEquation.getFormula(getParameters());
	}
	
	public Set<ResponseValue> getResponseValues() {
		return responseValues;
	}

	protected void setResponseValues(Set<ResponseValue> responseValues) {
		this.responseValues.clear();
		this.responseValues.addAll(responseValues);
	}
	
	public void addResponseValue(ResponseValue responseValue) {
		if (responseValue!=null)
			responseValue.setCalculatedCalibrationCurve(this);
	}
	
	public void removeResponseValue(ResponseValue responseValue) {
		if (responseValue!=null && this.responseValues.contains(responseValue))
			responseValue.setCalculatedCalibrationCurve(null);
	}

	@Override
	public Set<Sample> calculateSample(Set<Sample> samples) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<BigDecimal> calculateParameters() throws FunctionStrategyException {
		if (iCalculateEquation==null) throw new FunctionStrategyException("Function Strategy must not be null");
		getResponseValues()
		.forEach(obj -> obj.setCalculationResponseValueStrategy(calculationResponseValueStrategy));
		return iCalculateEquation.calculateParameters(getResponseValues(), weight);
	}
	
	public Optional<BigDecimal> calculateCoefficientOfDetermination() throws FunctionStrategyException {
		if (iCalculateEquation==null) throw new FunctionStrategyException("Function Strategy must not be null");
		getResponseValues()
		.forEach(obj -> obj.setCalculationResponseValueStrategy(calculationResponseValueStrategy));
		return iCalculateEquation.calculateCoefficientOfDetermination(getResponseValues(), getParameters());
	}
	
	public WEIGHT getWeight() {
		return weight;
	}

	public void setWeight(WEIGHT weight) {
		this.weight = weight;
	}

	public ICalculationResponseValueStrategy getCalculationResponseValueStrategy() {
		return calculationResponseValueStrategy;
	}

	public void setCalculationResponseValueStrategy(ICalculationResponseValueStrategy calculationResponseValueStrategy) {
		this.calculationResponseValueStrategy = calculationResponseValueStrategy;
	}
	
	public InternalStandard getInternalStandard() {
		return internalStandard;
	}

	public void setInternalStandard(InternalStandard internalStandard) {
		this.internalStandard = internalStandard;
	}

	/**
	 * Clones an CalculatedCalibrationCurve instance without id and all relations are null.
	 */
	@Override
	protected CalculatedCalibrationCurve clone() throws CloneNotSupportedException {
		var clone = new CalculatedCalibrationCurve();
		clone.setId(null);
		clone.setCalculationResponseValueStrategy(getCalculationResponseValueStrategy());
		clone.setiCalculateEquation(getiCalculateEquation());
		clone.setWeight(getWeight());
		clone.setInternalStandard(internalStandard);
		return clone;
	}
	
	

}
