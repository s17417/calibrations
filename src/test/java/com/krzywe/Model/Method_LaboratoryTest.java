package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class Method_LaboratoryTest {
	
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
		
		method.setAnalyte(analyte);
	}
	
	@Test
	public void checkAddMethod() {
		laboratoryTest.addMethod(method);
				
		assertTrue(
				method.getAnalyte().equals(analyte)&& 
				laboratoryTest.getMethod().contains(method)
				);
		
		testEntityManager.persist(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
	}
	
	@Test
	public void checkAddAnalyte() {
		method.setLaboratoryTest(laboratoryTest);
		
		assertTrue(
				method.getAnalyte().equals(analyte)&& 
				laboratoryTest.getMethod().contains(method)
				);
		
		assertDoesNotThrow(() -> testEntityManager.flush());
	}

	@Test
	public void checkRemoveMethod() {
		
		var analyte2 = new Analyte();
		analyte2.setName("ggt");
		testEntityManager.persist(analyte2);
		
		var method2 = new Method(analyte2, laboratoryTest);
		
		laboratoryTest.addMethod(method);
		laboratoryTest.addMethod(method2);
		
		laboratoryTest.removeMethod(method);
		assertTrue(
				method.getLaboratoryTest()==null&&
				laboratoryTest.getMethod().size()==1&&
				!laboratoryTest.getMethod().contains(method)
				);
		
		laboratoryTest.addMethod(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		analyte.removeMethod(method);
		laboratoryTest.removeMethod(method);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		assertNull(testEntityManager.find(Method.class, method.getId()));
		assertTrue(
				testEntityManager.find(LaboratoryTest.class, laboratoryTest.getId()).getMethod().size()==1&&
				testEntityManager.find(LaboratoryTest.class, laboratoryTest.getId()).getMethod().contains(method2)
				);	
	}
}
