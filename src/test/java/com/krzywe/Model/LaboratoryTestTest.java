package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
public class LaboratoryTestTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	private LaboratoryTest laboratoryTest;
	
	@BeforeEach
	private void beforeEach() {
		laboratoryTest = new LaboratoryTest();
		laboratoryTest.setName("laboratory Test");
		laboratoryTest.setMaterialType(MaterialType.DBS);
	}
	
	public void checkCorrectLaboratoryTestValues() {
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(laboratoryTest));
	}
	
	@Test
	public void checkLaboratoryTestName() {
		testEntityManager.persist(laboratoryTest);
		
		laboratoryTest.setName(null);
		var exp = assertThrows(ConstraintViolationException.class, () ->testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
		
		laboratoryTest.setName(" ");
		exp = assertThrows(ConstraintViolationException.class, () ->testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
		
		laboratoryTest.setName("1");
		exp = assertThrows(ConstraintViolationException.class, () ->testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
		
		laboratoryTest.setName(Strings.repeat("q", 101));
		exp = assertThrows(ConstraintViolationException.class, () ->testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
		
	}
	
	public void checkLaboratoryTestMaterialType() {
		testEntityManager.persist(laboratoryTest);
		
		laboratoryTest.setMaterialType(null);
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
	}
}
