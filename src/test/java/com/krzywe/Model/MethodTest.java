package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class MethodTest {
	
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private Method method;
	
	private Analyte analyte;
	
	private LaboratoryTest laboratoryTest;
	
	@BeforeEach
	public void beforeEach() {
		analyte = new Analyte();
		laboratoryTest = new LaboratoryTest();
		method = new Method();
		method.setResponseValueUnits("L");
		method.setAnalyticalMethod("UVVIS");
		analyte.setName(Strings.repeat("qw", 2));
		laboratoryTest.setName(Strings.repeat("fd", 2));
		laboratoryTest.setMaterialType(MaterialType.LEUKOCYTES);

		testEntityManager.persist(laboratoryTest);
		testEntityManager.persist(analyte);
		
		method.setAnalyte(analyte);
		method.setLaboratoryTest(laboratoryTest);
		testEntityManager.persist(method);
	}
	
	@Test
	public void checkCorrectMethodValues() {
		assertDoesNotThrow(() -> testEntityManager.flush());
	}
	
	@Test
	public void checkResponseValueUnits() {
		method.setResponseValueUnits(null);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		method.setResponseValueUnits(Strings.repeat("La", 51));
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - up to 100 chars")));
		
	}
	
	public void checkAnalyticalMethod() {
		method.setAnalyticalMethod(null);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		method.setAnalyticalMethod("LA");
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
		
		method.setAnalyticalMethod(Strings.repeat("La", 51));
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
	}

}
