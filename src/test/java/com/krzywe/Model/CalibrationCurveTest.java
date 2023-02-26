package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import javax.validation.Validation;
import javax.validation.Validator;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalibrationCurveTest {
	
	private CalibrationCurve calibrationCurve;
	
	@Mock
	private CalibrationCurveCalType calibrationCurveCalType;
	
	@Mock
	private CalibrationResultSet calibrationResultSet;
	
	@Mock
	private Method method;
	
	@Mock
	private Method secondaryQuantMethod;
	
	private Validator validator;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		calibrationCurve = new CalibrationCurve();
		calibrationCurve.setCalibrationCurveCalType(calibrationCurveCalType);
		calibrationCurve.setCalibrationResultSet(calibrationResultSet);
		calibrationCurve.setMethod(method);
	}
	
	@Test
	public void checkCorrectCalibrationCurveValues() {
		assertTrue(validator.validate(calibrationCurve).isEmpty());
	}
	
	@Test
	public void checkCalibrationCurveCalibrationResultSet_NotNull() {
		when(calibrationResultSet.getCalibrationCurves())
		.thenReturn(Sets.set(calibrationCurve));
		
		calibrationCurve.setCalibrationResultSet(null);
		var exp = validator.validate(calibrationCurve);
		assertTrue(!exp.isEmpty());
		exp.stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty"));
	}
	
	@Test
	public void checkCalibrationCurveCalibrationCurveCalType_NotNull() {
		calibrationCurve.setCalibrationCurveCalType(null);
		var exp = validator.validate(calibrationCurve);
		assertTrue(!exp.isEmpty());
		exp.stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty"));
	}
	
	@Test
	public void checkCalibrationCurveMethod_NotNull() {
		calibrationCurve.setMethod(null);
		var exp = validator.validate(calibrationCurve);
		assertTrue(!exp.isEmpty());
		exp.stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty"));
	}
	
	@Test
	public void checkCalibrationCurve_Clone() throws CloneNotSupportedException {
		this.calibrationCurve.addSecondaryQuantMethod(secondaryQuantMethod);
		var clone = this.calibrationCurve.clone();
		assertTrue(clone.getMethod().equals(calibrationCurve.getMethod()));
		assertTrue(clone.getId()==null);
		assertTrue(
				clone
				.getSecondaryQuantMethod()
				.stream()
				.allMatch(obj -> calibrationCurve.getSecondaryQuantMethod().contains(obj))
				);
	}
	
	@Test
	public void checkCalibrationCurve_addSecondaryMethod() {
		this.calibrationCurve.addSecondaryQuantMethod(secondaryQuantMethod);
		assertTrue(calibrationCurve.getSecondaryQuantMethod().contains(secondaryQuantMethod));
	}
	
	@Test
	public void checkCalibrationCurve_removeSecondaryMethod() {
		this.calibrationCurve.addSecondaryQuantMethod(secondaryQuantMethod);
		this.calibrationCurve.removeSecondaryQuantMethod(secondaryQuantMethod);
		assertTrue(!calibrationCurve.getSecondaryQuantMethod().contains(secondaryQuantMethod));
	}
	
	@Test
	public void checkCalibrationCurve_setCalibrationResultSet() {
		when(calibrationResultSet.getCalibrationCurves()).thenReturn(Sets.newHashSet());
		this.calibrationCurve.setCalibrationResultSet(calibrationResultSet);
		assertTrue(calibrationCurve.getCalibrationResultSet().equals(calibrationResultSet));
	}
}
