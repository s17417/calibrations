package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CalibrationResultSetTest {

	
	private Validator validator;
	
	private CalibrationResultSet calibrationResultSet;
	
	@Mock
	private CalibrationSet calibrationSet;
	
	@Mock
	private CalibrationCurve calibrationCurve;
	
	@Mock
	private CalibrationCurveCalType calibrationCurveCalType;
	
	@Mock
	private LaboratoryTest laboratoryTest;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		when(calibrationSet.getMaterialType()).thenReturn(MaterialType.CSF);
		when(laboratoryTest.getMaterialType()).thenReturn(MaterialType.CSF);
		calibrationResultSet = new CalibrationResultSet(calibrationSet,laboratoryTest);
		calibrationResultSet.setName("2022_CSF");
		
	}
	
	@Test
	public void checkCorrectCalibrationResultSetValues() {
		assertTrue(() -> validator.validate(calibrationResultSet).isEmpty());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	public void checkCalibrationResultSetName_NotBlank(String name) {
		calibrationResultSet.setName(name);
		var exp = validator.validate(calibrationResultSet);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid field can't be empty")));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"q"})
	@MethodSource("longStringSupplier")
	public void checkCalibrationResultSetName_Size(String name) {
		calibrationResultSet.setName(name);
		var exp = validator.validate(calibrationResultSet);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid length - 3 to 50 chars")));
	}
	
	public Stream<String> longStringSupplier() {
		return Stream.of(Strings.repeat("q", 51));
	}
	
	@ParameterizedTest
	@NullSource
	public void checkCalibrationResultSetIsTemplate_NotNull(String name) {
		calibrationResultSet.setName(name);
		var exp = validator.validate(calibrationResultSet);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid field can't be empty")));
	}
	
	@ParameterizedTest
	@NullSource
	public void checkCalibrationResultSetCalibrationSet_NotNull(CalibrationSet calibrationSet) {
		calibrationResultSet.setCalibrationSet(calibrationSet);
		var exp = validator.validate(calibrationResultSet);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid field can't be empty")));
	}
	
	@ParameterizedTest
	@NullSource
	public void checkCalibrationResultSetLaboratoryTest_NotNull(LaboratoryTest laboratoryTest) {
		calibrationResultSet.setLaboratoryTest(laboratoryTest);
		var exp = validator.validate(calibrationResultSet);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid field can't be empty")));
	}
	
	@Test
	public void checkCalibrationResultSet_addCalibrationCurve() {
		doAnswer(
				invocation -> {
					invocation
					.getArgument(0, CalibrationResultSet.class)
					.getCalibrationCurves()
					.add(calibrationCurve);
					return null;
				}
				).when(calibrationCurve).setCalibrationResultSet(any(CalibrationResultSet.class));
		calibrationResultSet.addCalibrationCurve(calibrationCurve);
		
		assertTrue(!calibrationResultSet.getCalibrationCurves().isEmpty());
		assertTrue(calibrationResultSet.getCalibrationCurves().contains(calibrationCurve));	
	}
	
	public void checkCalibrationResultSet_removeCalibrationcurve() {
		doAnswer(invocation -> {
			calibrationResultSet
			.getCalibrationCurves()
			.remove(calibrationCurve);
			return null;
		}).when(calibrationCurve).setCalibrationResultSet(isNull());
		calibrationResultSet.getCalibrationCurves().add(calibrationCurve);
		calibrationResultSet.removeCalibrationCurve(calibrationCurve);
		
		assertTrue(validator.validate(calibrationResultSet).isEmpty());
		assertTrue(!calibrationResultSet.getCalibrationCurves().contains(calibrationCurve));
	}
	
	@Test
	public void checkCalibrationResultSetClone_ThrowsCloneNotSupportedException() {
		assertThrows(CloneNotSupportedException.class, () -> calibrationResultSet.clone());
	}
	
	@Test
	public void checkCalibrationResultSet_Clone() throws CloneNotSupportedException {
		calibrationResultSet.setIsTemplate(true);
		assertDoesNotThrow(() -> calibrationResultSet.clone());
		CalibrationResultSet clone = calibrationResultSet.clone();
		assertTrue(() -> clone.getName()==null);
		assertTrue(() -> !clone.getIsTemplate().equals(calibrationResultSet.getIsTemplate()));
		assertTrue(() -> clone.getCalibrationSet().equals(calibrationResultSet.getCalibrationSet()));
		assertTrue(() -> clone.getLaboratoryTest().equals(calibrationResultSet.getLaboratoryTest()));
		assertTrue(() -> clone.getId()==null);
		assertTrue(() -> clone.getCalibrationCurves().isEmpty());
	}
	
	@Test
	public void checkCalibrationResultSet_createTemplate() throws CloneNotSupportedException {
		calibrationResultSet.setIsTemplate(true);
		calibrationResultSet.getCalibrationCurves().add(calibrationCurve);
		when(calibrationCurve.clone()).thenReturn(calibrationCurve);
		when(calibrationCurveCalType.clone()).thenReturn(calibrationCurveCalType);
		
		when(calibrationCurve.getCalibrationCurveCalType()).thenReturn(calibrationCurveCalType);
		
		doAnswer(
				invocation -> {
					invocation
					.getArgument(0, CalibrationResultSet.class)
					.getCalibrationCurves()
					.add(calibrationCurve);
					return null;
				}
				).when(calibrationCurve).setCalibrationResultSet(any(CalibrationResultSet.class));	
		
		assertTrue(calibrationResultSet.createTemplate()!=null);
		assertTrue(!calibrationResultSet.createTemplate().getCalibrationCurves().isEmpty());
	}
	
}
