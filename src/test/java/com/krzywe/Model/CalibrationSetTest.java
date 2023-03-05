package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import java.time.LocalDate;
import java.util.stream.Stream;

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
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalibrationSetTest {
	
	private Validator validator;
	
	private CalibrationSet calibrationSet;
	
	@Mock
	private CalibrationPoint calibrationPoint; 
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		calibrationSet = new CalibrationSet();
		calibrationSet.setName("2022/01/01_New Calibration Standard");
		calibrationSet.setPreparationDate(LocalDate.now());
		calibrationSet.setExpirationDate(LocalDate.now());
		calibrationSet.setMaterialType(MaterialType.CSF);
	}
	
	@Test
	public void checkCorrectCalibrationSet_Values() {
		assertTrue(() -> validator.validate(calibrationSet).isEmpty());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings= {" ", "2"})
	@MethodSource("longStringSupplier")
	public void checkCalibrationSet_Name(String name) {
		calibrationSet.setName(name);
		
		var exception = validator.validate(calibrationSet);
		
		assertTrue(
				exception
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(
						obj -> obj.contains("valid field can't be empty")||
						obj.contains("valid length - 3 to 50 chars")
						)
				);	
	}
	
	private Stream<String> longStringSupplier() {
		return Stream.of(Strings.repeat("aa", 51));
	}
	
	@ParameterizedTest
	@NullSource
	public void checkCalibrationSet_PreparationDateNullable(LocalDate date) {
		calibrationSet.setPreparationDate(date);
		assertTrue(validator.validate(calibrationSet).isEmpty());
	}
	
	@Test
	public void checkCalibrationSet_PreparationDateFutureDate() {
		calibrationSet.setPreparationDate(LocalDate.now().plusDays(1));
		
		var exception = validator.validate(calibrationSet);
		
		assertTrue(
				exception
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid date from past or present"))
				);
	}

	@ParameterizedTest
	@NullSource
	public void checkCalibrationSet_ExpirationDateNullable(LocalDate date) {
		calibrationSet.setExpirationDate(date);
		assertTrue(validator.validate(calibrationSet).isEmpty());
	}
	
	@Test
	public void checkCalibrationSet_ExpirationDatePastDate() {
		calibrationSet.setExpirationDate(LocalDate.now().minusDays(1));
		
		var exception = validator.validate(calibrationSet);
		
		assertTrue(
				exception
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid date from future or present"))
				);
	}
	
	@ParameterizedTest
	@NullSource
	public void checkCalibrationSet_MaterialTypeNullable(MaterialType materialType) {
		calibrationSet.setMaterialType(materialType);
		
		var exception = validator.validate(calibrationSet);
		
		assertTrue(
				exception
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> obj.contains("valid field can't be empty")));	
	}
	
	@Test
	public void checkCalibrationSet_addCalibrationPoint() {	
		doAnswer(invocation -> {
			invocation
			.getArgument(0, CalibrationSet.class)
			.getCalibrationPoints()
			.add(calibrationPoint);
		return null;	
		}).when(calibrationPoint).setCalibrationSet(any(CalibrationSet.class));
		calibrationSet.addCalibrationPoint(calibrationPoint);
		
		assertTrue(validator.validate(calibrationSet).isEmpty());	
		assertTrue(calibrationSet.getCalibrationPoints().contains(calibrationPoint));
	}
	
	@Test
	public void checkCalibrationSet_removeCalibrationPoint() {	
		doAnswer(invocation -> {
			calibrationSet
			.getCalibrationPoints()
			.remove(calibrationPoint);
		return null;	
		}).when(calibrationPoint).setCalibrationSet(isNull());
		calibrationSet.getCalibrationPoints().add(calibrationPoint);
		calibrationSet.removeCalibrationPoint(calibrationPoint);
		
		assertTrue(validator.validate(calibrationSet).isEmpty());	
		assertTrue(!calibrationSet.getCalibrationPoints().contains(calibrationPoint));
	}
	


}
