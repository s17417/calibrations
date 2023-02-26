package com.krzywe.Model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(
				name = "UK_CALIBRATION_CURVE_CALIBRATION_SET_ID_METHOD_ID",
				columnNames = {"method_laboratoryTest_id","method_analyte_id","calibrationResultSet_id"}
				)
		}
)
public class CalibrationCurve extends AbstractPersistentObject implements Cloneable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@OneToOne(
			fetch = FetchType.LAZY,
			mappedBy = "calibrationCurve",
			orphanRemoval = true,
			cascade = CascadeType.ALL
			)
	@PrimaryKeyJoinColumn
	private CalibrationCurveCalType calibrationCurveCalType;

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
	@JoinColumns(foreignKey = @ForeignKey(
			name="FK_CALIBRATIONCURVE_METHOD_ID",
			foreignKeyDefinition = "FOREIGN KEY (method_laboratoryTest_id, method_analyte_id) "
					+ "REFERENCES Method (laboratoryTest_id, analyte_id) "
					+ "ON DELETE CASCADE"
			),value={
		@JoinColumn(nullable = false, name="method_laboratoryTest_id", referencedColumnName="laboratoryTest_id"),
		@JoinColumn(nullable = false, name="method_analyte_id", referencedColumnName="analyte_id")
	})
	private Method method;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "CalibrationCurve_Method",
			joinColumns = {
					@JoinColumn(nullable = false, name = "calibration_curve_id", referencedColumnName = "id")
					},
			inverseJoinColumns = {
					@JoinColumn(nullable = false, name="secondary_quant_method_laboratoryTest_id", referencedColumnName="laboratoryTest_id"),
					@JoinColumn(nullable = false, name="secondary_quant_method_analyte_id", referencedColumnName="analyte_id")
					},
			inverseForeignKey = @ForeignKey(
					name="FK_SECONDARY_METHOD_ID_CALIBRATIONCURVE",
					foreignKeyDefinition = "FOREIGN KEY (secondary_quant_method_laboratoryTest_id, secondary_quant_method_analyte_id) "
							+ "REFERENCES Method (laboratoryTest_id, analyte_id) "
							+ "ON DELETE CASCADE"
					),
			foreignKey = @ForeignKey(name = "FK__SECONDARY_METHOD_CALIBRATIONCURVE_ID")
	)
	private Set<Method> secondaryQuantMethod = new HashSet<>();

	protected CalibrationCurve() {
		super();
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

	public CalibrationCurveCalType getCalibrationCurveCalType() {
		return calibrationCurveCalType;
	}

	public void setCalibrationCurveCalType(CalibrationCurveCalType calibrationCurveCalType) {
		if (calibrationCurveCalType==null) {
			if ( this.calibrationCurveCalType!=null)
				this.calibrationCurveCalType.setCalibrationCurve(null);
		} else {
			calibrationCurveCalType.setCalibrationCurve(this);
		}
		this.calibrationCurveCalType=calibrationCurveCalType;
	}
	
	public Set<Method> getSecondaryQuantMethod() {
		return secondaryQuantMethod;
	}

	public void setSecondaryQuantMethod(Set<Method> secondaryQuantMethod) {
		this.secondaryQuantMethod = secondaryQuantMethod;
	}
	
	public void addSecondaryQuantMethod(Method method) {
		secondaryQuantMethod.add(method);
	}
	
	public void removeSecondaryQuantMethod(Method method) {
		secondaryQuantMethod.remove(method);
	}

	/**
	 * Clones an CalibrationCurve instance without id and all relations are null besides Method class.
	 */
	@Override
	protected CalibrationCurve clone() throws CloneNotSupportedException {
		var clone = new CalibrationCurve();
		clone.setId(null);
		clone.setMethod(this.getMethod());
		clone.setSecondaryQuantMethod(secondaryQuantMethod);
		return clone;
	}
	
	
	
	

}
