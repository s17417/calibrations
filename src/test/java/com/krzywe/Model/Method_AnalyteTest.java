package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class Method_AnalyteTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private LaboratoryTest laboratoryTest;
	
	private Analyte analyte;
	
	private Method method;
	
	@BeforeEach
	public void beforeEach() {
		laboratoryTest = new LaboratoryTest();
		laboratoryTest.setName("ddd");
		laboratoryTest.setMaterialType(MaterialType.CSF);
		
		analyte = new Analyte();
		analyte.setName("gkg");
		
		method = new Method();
		
		testEntityManager.persist(analyte);
		testEntityManager.persist(laboratoryTest);
		
		method.setLaboratoryTest(laboratoryTest);
	}
	
	@Test
	public void checkAddMethod() {
		analyte.addMethod(method);
				
		assertTrue(
				method.getAnalyte().equals(analyte)&& 
				analyte.getMethod().contains(method)
				);
		
		testEntityManager.persist(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
	}
	
	@Test
	public void checkAddAnalyte() {
		method.setAnalyte(analyte);
		
		assertTrue(
				method.getAnalyte().equals(analyte)&& 
				analyte.getMethod().contains(method)
				);

		assertDoesNotThrow(() -> testEntityManager.flush());
	}
	
	@Test
	public void checkRemoveMethod() {
		
		var laboratoryTest2 = new LaboratoryTest();
		laboratoryTest2.setName("ggt");
		laboratoryTest2.setMaterialType(MaterialType.ERYTHROCYTES);
		testEntityManager.persist(laboratoryTest2);
		
		var method2 = new Method(analyte, laboratoryTest2);
		
		analyte.addMethod(method);
		analyte.addMethod(method2);
		
		analyte.removeMethod(method);
		assertTrue(
				method.getAnalyte()==null&&
				analyte.getMethod().size()==1&&
				!analyte.getMethod().contains(method)
				);
		
		analyte.addMethod(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		analyte.removeMethod(method);
		laboratoryTest.removeMethod(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		assertNull(testEntityManager.find(Method.class, method.getId()));
		assertTrue(
				testEntityManager.find(Analyte.class, analyte.getId()).getMethod().size()==1&&
				testEntityManager.find(Analyte.class, analyte.getId()).getMethod().contains(method2)
				);	
	}

}
