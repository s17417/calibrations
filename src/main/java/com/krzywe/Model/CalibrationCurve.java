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
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Digits;

import com.sun.istack.NotNull;


@Entity
public class CalibrationCurve extends AbstractPersistentObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Convert(converter = CalculateEquationConverter.class)
	private ICalculateEquation iCalculateEquation;
	
	private BigDecimal coefficientOfDetermination;
	
	@OrderColumn
	@ElementCollection
	@Column(precision = 20, scale=12)
	private List<@Digits(fraction = 12, integer = 20) BigDecimal> parameters = new ArrayList<>();
	
	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = {CascadeType.ALL}
			)
	@JoinColumn(
			foreignKey = @ForeignKey(
					name = "FK_CALIBRATIONCURVE_RESPONSEVALUE_ID"
					)
			)
	private Set<ResponseValue>responseValues = new HashSet<>();
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(
			nullable = false,
			foreignKey = @ForeignKey(
					name = "FK_CALIBRATIONCURVE_CALIBRATIONRESULTSET_ID"
					)
			)
	private CalibrationResultSet calibrationResultSet;
	
	@NotNull
	@ManyToOne
	@JoinColumns({
		@JoinColumn(
				nullable = false,
				foreignKey = @ForeignKey(
						name = "FK_CALIBRATIONCURVE_METHOD_LABORATORYTEST_ID",
						foreignKeyDefinition = 
								"FOREIGN KEY (laboratoryTest_Id)"
								+ "REFERENCES Method (laboratoryTest_Id)"
								+ "ON DELETE CASCADE"
						)
				),
		@JoinColumn(
				nullable = false,
				foreignKey = @ForeignKey(
						name = "FK_CALIBRATIONCURVE_METHOD_ANALYTE_ID",
						foreignKeyDefinition = 
								"FOREIGN KEY (analyte_Id)"
								+ "REFERENCES Method (analyte_Id)"
								+ "ON DELETE CASCADE"
						)
				)
	})
	private Method method;

	protected CalibrationCurve() {
		super();
	}

	public ICalculateEquation getiCalculateEquation() {
		return iCalculateEquation;
	}

	public void setiCalculateEquation(ICalculateEquation iCalculateEquation) {
		this.iCalculateEquation = iCalculateEquation;
	}

	public CalibrationResultSet getCalibrationResultSet() {
		return calibrationResultSet;
	}

	public void setCalibrationResultSet(CalibrationResultSet calibrationResultSet) {
		if (this.calibrationResultSet == null) {
			if (calibrationResultSet == null) return;
			calibrationResultSet.getCalibrationCurves().add(this);
				this.calibrationResultSet=calibrationResultSet;
				return;
		}
		if (this.calibrationResultSet.getCalibrationCurves().remove(this)) {
			this.calibrationResultSet=calibrationResultSet;
			if (this.calibrationResultSet!=null)
				this.calibrationResultSet.getCalibrationCurves().add(this);
		}
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public List<BigDecimal> getParameters() {
		return parameters;
	}

	public void setParameters(List<BigDecimal> parameters) {
		this.parameters.clear();
		this.parameters.addAll(iCalculateEquation.setParameters(parameters));
	}

	public BigDecimal getCoefficientOfDetermination() {
		return coefficientOfDetermination;
	}

	public void setCoefficientOfDetermination(BigDecimal coefficientOfDetermination) {
		this.coefficientOfDetermination = coefficientOfDetermination;
	}

	public Set<ResponseValue> getResponseValues() {
		return responseValues;
	}

	public void setResponseValues(Set<ResponseValue> responseValues) {
		this.responseValues.clear();
		this.responseValues.addAll(responseValues);
	}
	
	public void addResponseValue(ResponseValue responseValue) {
		if (responseValue!=null)
			responseValue.setCalibrationCurve(this);
	}
	
	public void removeResponseValue(ResponseValue responseValue) {
		if (responseValue!=null && this.responseValues.contains(responseValue))
			responseValue.setCalibrationCurve(null);
	}
	
	public List<BigDecimal> calculateParameters() {
		return iCalculateEquation.calculateParameters(responseValues);
	}
	
	public Optional<BigDecimal> calculateCoefficientOfDetermination(){
		return iCalculateEquation.calculateCoefficientOfDetermination(responseValues, parameters);
	}
	
	public Optional<String> getEquation(){
		return iCalculateEquation.getFormula(parameters);
	}
	
	

}
