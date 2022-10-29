package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AnalyteTest {
	
	@Autowired
	private TestEntityManager testEntityManager;
	
	private Analyte analyte;
	
	private Random rand = new Random();
	
	Supplier<String> stringSupplier = () -> 
	rand
	.ints(97,123)
	.limit(4)
	.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	.toString();
	
	@BeforeEach
	public void beforeEach() {
		analyte = new Analyte();
		analyte.setName("Citrulline");
	}
	
	@Test
	public void checkCorrectAnalyteValues() {
		assertDoesNotThrow(() -> testEntityManager.persistAndFlush(analyte));
	}
	
	@Test
	public void checkAnalyteName() {
		testEntityManager.persist(analyte);
		
		analyte.setName(null);
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
		
		analyte.setName(" ");
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
		
		analyte.setName("1");
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
		
		analyte.setName(Strings.repeat("qa", 51));
		exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));	
	}
	
	@Test
	public void checkAnalyteAliases() {
		var list = Stream.generate(stringSupplier).limit(10).collect(Collectors.toList());
		
		testEntityManager.persist(analyte);
		analyte.setAliases(list);
		assertDoesNotThrow(() -> testEntityManager.flush());
		
		//check order
		assertThat(analyte.getAliases())
		.containsExactly(
				list
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				.toArray(new String[list.size()])
				);
		
		//check remove of alias	
				analyte.removeAlias(list.remove(1).toUpperCase());
				assertDoesNotThrow(() -> testEntityManager.flush());
		
		//check list size and String length
		analyte.setAliases(Stream.generate(stringSupplier).limit(33).collect(Collectors.toList()));
		analyte.addAlias(Strings.repeat("qq", 17));		
		var exp = assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
		assertTrue(() -> exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid collection size - up to 32 aliases")));
		assertTrue(() -> exp.getConstraintViolations().stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 32 chars")));
		
	}
}
