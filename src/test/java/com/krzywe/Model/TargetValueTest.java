package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TargetValueTest {
	
	private  Validator validator;
	
	private TargetValue targetValue = new TargetValue();
	
	@Mock
	private CalibrationPoint calibrationPoint;
	
	@Mock
	private Analyte analyte;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		when(calibrationPoint.getTargetValues()).thenReturn(new HashSet<TargetValue>());
		targetValue.setTargetValue(BigDecimal.valueOf(10.0));
		targetValue.setUnits("umol/L");
		targetValue.setCalibrationPoint(calibrationPoint);
		targetValue.setAnalyte(analyte);
	}
	
	@Test
	public void checkCorrectTargetValue_Values() {
		assertTrue(() -> validator.validate(targetValue).isEmpty());
	}
	
	@Test
	public void checkTargetValue_IsNullable() {
		targetValue.setTargetValue(null);
		assertTrue(() -> validator.validate(targetValue).isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"0.00000000000001", "100000000000000000000"})
	public void checkTargetValue_MaxFractionAndIntegerLength(String value) {
		targetValue.setTargetValue(new BigDecimal(value));
		
		var exp = validator.validate(targetValue);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("numeric value out of bounds (<20 digits>.<12 digits> expected)"))
				);
	}
	
	@Test
	public void checkTargetValue_UnitsIsNullable() {
		targetValue.setUnits(null);
		assertTrue(() -> validator.validate(targetValue).isEmpty());
	}
	
	@Test
	public void checkTargetValue_UnitsMaxLength() {
		targetValue.setUnits(Strings.repeat("a", 51));
		
		var exp = validator.validate(targetValue);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid length - up to 50 chars"))
				);
	}
	
	@ParameterizedTest
	@NullSource
	public void checkTargetValue_AnalyteNotNull(Analyte analyte) {
		targetValue.setAnalyte(analyte);
		
		var exp = validator.validate(targetValue);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid field can't be empty"))
				);
	}
	
	@ParameterizedTest
	@NullSource
	public void checkTargetValue_CalibrationPointNotNull(CalibrationPoint calibrationPoint) {
		targetValue.setCalibrationPoint(calibrationPoint);
		
		var exp = validator.validate(targetValue);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid field can't be empty"))
				);
	}
	
	@Test
	public void checkTargetValue_CalibrationPoint() {
		targetValue.setCalibrationPoint(calibrationPoint);
		
		var exp = validator.validate(targetValue);
		
		assertTrue(exp.isEmpty());
		assertTrue(targetValue.getCalibrationPoint().equals(calibrationPoint));
	}
	
}
