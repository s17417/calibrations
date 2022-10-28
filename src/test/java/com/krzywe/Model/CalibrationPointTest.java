package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalibrationPointTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private CalibrationSet calibrationSet;
	
	private CalibrationPoint calibrationPoint;
	
	Random rand= new Random();
	
	Supplier<String> stringSupplier = () -> 
			rand
			.ints(97,123)
			.limit(4)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	
	@BeforeEach
	public void beforeEach() {
		calibrationSet = new CalibrationSet();
		calibrationSet.setName("1234");
		calibrationSet.setMaterialType(MaterialType.CSF);
		testEntityManager.persistAndFlush(calibrationSet);
		
		calibrationPoint = new CalibrationPoint();
		calibrationPoint.setPointId("1/2022/09");
		calibrationPoint.setCalibrationSet(calibrationSet);
	}
	
	@Test
	public void checkCorrectCalibrationPointValues() {
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(calibrationPoint));
	}
	
	@Test
	public void checkCalibrationPointPointId() {
		calibrationPoint.setPointId(null);
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationPoint));
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field cannot be empty")));

		calibrationPoint.setPointId(" ");
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationPoint));
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field cannot be empty")));
		
		calibrationPoint.setPointId("12");
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationPoint));
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 32 chars")));	
		
		calibrationPoint.setPointId(Strings.repeat("1", 33));
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(calibrationPoint));
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 32 chars")));			
	}
	
	@Test
	public void checkCalibrationPointAliases() {
		List<String> list = new ArrayList<>(Arrays.asList(new String[]{"phe","hhf","fff"}));
		calibrationPoint.setAliases(list);
		calibrationPoint.addAlias("koldds");
		calibrationPoint.addAlias("opo");
		calibrationPoint.addAlias("opo");
		list.add("koldds");
		list.add("opo");
		
		//check order persisting
		calibrationPoint=assertDoesNotThrow(() -> testEntityManager.persistAndFlush(calibrationPoint));
		assertThat(calibrationPoint.getAliases())
		.containsExactly(
				list
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				.toArray(new String[list.size()])
				);
		
		//check of remove and persisting order
		calibrationPoint.removeAlias("koldds");
		list.remove("koldds");
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		assertThat(calibrationPoint.getAliases())
		.containsExactly(
				list
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				.toArray(new String[3])
				);
		
		//Constraint check of max number of objects in collection and their length
		calibrationPoint.setAliases(
				Stream
				.generate(stringSupplier)
				.limit(36)
				.collect(Collectors.toList())
				);
		calibrationPoint.addAlias(Strings.repeat("q", 33));
		var exp = assertThrows(ConstraintViolationException.class,() -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid collection size - up to 32 aliases")));
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 32 chars")));
		
		System.out.println(exp);
	}

}
