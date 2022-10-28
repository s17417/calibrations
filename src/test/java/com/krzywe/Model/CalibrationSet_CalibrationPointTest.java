package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalibrationSet_CalibrationPointTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private CalibrationSet calibrationSet1;
	
	private CalibrationSet calibrationSet2;
	
	private CalibrationPoint calibrationPoint1;
	
	private CalibrationPoint calibrationPoint2;
	
	@BeforeEach
	public void beforeEach() {

		calibrationSet1 = new CalibrationSet();
		calibrationSet1.setName("2022/01/01_New Calibration Standard");
		calibrationSet1.setPreparationDate(LocalDate.now());
		calibrationSet1.setExpirationDate(LocalDate.now());
		calibrationSet1.setMaterialType(MaterialType.CSF);
		
		calibrationSet2 = new CalibrationSet();
		calibrationSet2.setName("2021/01/01_New Calibration Standard");
		calibrationSet1.setPreparationDate(LocalDate.now());
		calibrationSet1.setExpirationDate(LocalDate.now());
		calibrationSet1.setMaterialType(MaterialType.CSF);
		
		calibrationPoint1 = new CalibrationPoint();
		calibrationPoint2 = new CalibrationPoint();
		
		calibrationPoint1.setPointId("12345-2021");
		calibrationPoint2.setPointId("12345-2022");
	}
	
	@Test
	public void checkAddCalibrationPoint() {
		assertThrows(NullPointerException.class, () -> calibrationSet1.addCalibrationPoint(null));
		assertThrows(NullPointerException.class, () -> calibrationSet1.removeCalibrationPoint(null));
		
		calibrationSet1.addCalibrationPoint(calibrationPoint1);
		calibrationSet1.addCalibrationPoint(calibrationPoint2);
		
		assertTrue(() -> 
			calibrationSet1.getCalibrationPoints().contains(calibrationPoint1)&&
			calibrationSet1.getCalibrationPoints().contains(calibrationPoint2)&&
			calibrationPoint1.getCalibrationSet().equals(calibrationSet1)&&
			calibrationPoint2.getCalibrationSet().equals(calibrationSet1)
		);	
		
		assertTrue(testEntityManager.persistFlushFind(calibrationSet1).getCalibrationPoints().containsAll(Arrays.asList(calibrationPoint1,calibrationPoint2)));
		assertTrue(testEntityManager.find(CalibrationPoint.class, calibrationPoint1.getId()).getCalibrationSet().equals(calibrationSet1));
		assertTrue(testEntityManager.find(CalibrationPoint.class, calibrationPoint2.getId()).getCalibrationSet().equals(calibrationSet1));		
	}
	
	@Test
	public void checkAddCalibrationSet() {
		calibrationPoint1.setCalibrationSet(calibrationSet1);
		calibrationPoint2.setCalibrationSet(calibrationSet1);
		
		assertTrue(() -> 
			calibrationSet1.getCalibrationPoints().contains(calibrationPoint1)&&
			calibrationSet1.getCalibrationPoints().contains(calibrationPoint2)&&
			calibrationPoint1.getCalibrationSet().equals(calibrationSet1)&&
			calibrationPoint2.getCalibrationSet().equals(calibrationSet1)
		);	
		
		calibrationSet1 = testEntityManager.persistAndFlush(calibrationSet1);
		calibrationPoint1 = testEntityManager.find(CalibrationPoint.class, calibrationPoint1.getId());
		calibrationPoint2 = testEntityManager.find(CalibrationPoint.class, calibrationPoint2.getId());		
	}
	
	@Test
	public void checkRemoveCalibrationPoint() {
		calibrationSet1.addCalibrationPoint(calibrationPoint1);
		calibrationSet1.addCalibrationPoint(calibrationPoint2);
		
		calibrationSet1.removeCalibrationPoint(calibrationPoint2);
		calibrationSet1 = testEntityManager.persistAndFlush(calibrationSet1);
		
		assertTrue(() -> 
			calibrationSet1.getCalibrationPoints().contains(calibrationPoint1)&&
			!calibrationSet1.getCalibrationPoints().contains(calibrationPoint2)&&
			calibrationPoint1.getCalibrationSet().equals(calibrationSet1)&&
			calibrationPoint2.getCalibrationSet()==null				
		);
		
		testEntityManager.remove(calibrationPoint1);
		assertNotNull(testEntityManager.find(CalibrationSet.class, calibrationSet1.getId()));
		assertNull(testEntityManager.find(CalibrationPoint.class, calibrationPoint1.getId()));
		assertNull(testEntityManager.find(CalibrationPoint.class, calibrationPoint2.getId()));
	}
	
	@Test
	public void checkRemoveCalibrationSet() {
		calibrationSet1.addCalibrationPoint(calibrationPoint1);
		calibrationSet1.addCalibrationPoint(calibrationPoint2);
		
		testEntityManager.persistAndFlush(calibrationSet1);
		testEntityManager.remove(calibrationSet1);
		
		assertNull(testEntityManager.find(CalibrationSet.class, calibrationSet1.getId()));
		assertNull(testEntityManager.find(CalibrationPoint.class, calibrationPoint1.getId()));
		assertNull(testEntityManager.find(CalibrationPoint.class, calibrationPoint2.getId()));
	}
	

}
