package com.krzywe.Model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_CALIBRATION_RESULT_SET_NAME",columnNames = { "name" })})
public class CalibrationResultSet extends AbstractPersistentObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Size(min = 3, max = 50)
	@Column(nullable = false, length = 50)
	private String name;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			updatable = false,
			foreignKey = @ForeignKey(
					name = "FK_CALIBRATIONRESULTSET_CALIBRATIONSET_ID",
					foreignKeyDefinition =
							"FOREIGN KEY (calibrationSet_id)"
							+ "REFERENCES CalibrationSet (Id)"
							+ "ON DELETE CASCADE"
					)
			)
	private CalibrationSet calibrationSet;
	
	@NotNull
	@ManyToOne
	@JoinColumn(
			nullable = false,
			updatable = false,
			foreignKey = @ForeignKey(
					name = "FK_CALIBRATIONRESULTSET_LABORATORYTEST_ID",
					foreignKeyDefinition = 
							"FOREIGN KEY (laboratoryTest_id)"
							+ "REFERENCES LaboratoryTest (Id)"
							+ "ON DELETE CASCADE"
					)
			)
	private LaboratoryTest laboratoryTest;
	
	@OneToMany(
			cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
			fetch = FetchType.LAZY,
			mappedBy = "calibrationResultSet",
			orphanRemoval = true
			)
	private Set<CalibrationCurve> calibrationCurves = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CalibrationSet getCalibrationSet() {
		return calibrationSet;
	}

	public void setCalibrationSet(CalibrationSet calibrationSet) {
		this.calibrationSet = calibrationSet;
	}

	public LaboratoryTest getLaboratoryTest() {
		return laboratoryTest;
	}

	public void setLaboratoryTest(LaboratoryTest laboratoryTest) {
		this.laboratoryTest = laboratoryTest;
	}

	public Set<CalibrationCurve> getCalibrationCurves() {
		return calibrationCurves;
	}

	public void setCalibrationCurves(Set<CalibrationCurve> calibrationCurve) {
		this.calibrationCurves.clear();
		this.calibrationCurves.addAll(calibrationCurve);
	}
	
	public void addCalibrationCurve(CalibrationCurve calibrationCurve) {
		if (calibrationCurve!=null)
			calibrationCurve.setCalibrationResultSet(this);
		
	}
	
	public void removeCalibrationCurve(CalibrationCurve calibrationCurve) {
		if (calibrationCurve!=null && this.calibrationCurves.contains(calibrationCurve))
			calibrationCurve.setCalibrationResultSet(this);
	}
	
}
