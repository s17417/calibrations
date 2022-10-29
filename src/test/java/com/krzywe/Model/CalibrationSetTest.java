package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class CalibrationSetTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private CalibrationSet calibrationSet;
	
	@BeforeEach
	public void beforeEach() {
		calibrationSet = new CalibrationSet();
		calibrationSet.setName("2022/01/01_New Calibration Standard");
		calibrationSet.setPreparationDate(LocalDate.now());
		calibrationSet.setExpirationDate(LocalDate.now());
		calibrationSet.setMaterialType(MaterialType.CSF);
	}
	
	@Test
	public void checkCorrectCalibrationSetValues() {
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(calibrationSet));
	}
	
	@Test
	public void checkCalibrationSetName() {
		calibrationSet.setName(null);
		var exception = assertThrows(ConstraintViolationException.class, ()->testEntityManager.persistAndFlush(calibrationSet));
		assertTrue(exception.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
		
		calibrationSet.setName("2");
		exception = assertThrows(ConstraintViolationException.class, () ->testEntityManager.persistAndFlush(calibrationSet));	
		assertTrue(exception.getConstraintViolations().stream().anyMatch( obj -> obj.getMessage().contains("valid length - 3 to 50 chars")));
		
		calibrationSet.setName(Strings.repeat("aa", 51));
		exception = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationSet));
		assertTrue(exception.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 50 chars")));		
	}
	
	@Test
	public void checkCalibrationSetPreparationDate() {
		calibrationSet.setPreparationDate(null);
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(calibrationSet));
		
		calibrationSet.setPreparationDate(LocalDate.now().plusDays(1));
		var exception = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exception.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid date from past or present")));
	}
	
	@Test
	public void checkCalibrationSetExpirationDate() {
		calibrationSet.setExpirationDate(null);
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(calibrationSet));
		
		calibrationSet.setExpirationDate(LocalDate.now().minusDays(1));
		var exception = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exception.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid date from future or present")));
	}
	
	@Test
	public void checkCalibrationSetMaterialType() {
		calibrationSet.setMaterialType(null);
		var exception = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationSet));
		assertTrue(exception.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));	
	}

}
