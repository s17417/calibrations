package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;

import java.util.stream.Stream;

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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LaboratoryTestTest {
	
	private Validator validator;
	
	@Mock
	private Method method;
	
	private LaboratoryTest laboratoryTest;
	
	@BeforeAll
	public void beforeAll(){
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		laboratoryTest = new LaboratoryTest();
		laboratoryTest.setName("laboratory Test");
		laboratoryTest.setMaterialType(MaterialType.DBS);
	}
	
	@Test
	public void checkCorrectLaboratoryTestValues() {
		assertTrue(validator.validate(laboratoryTest).isEmpty());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ","1"})
	@MethodSource("longStringSupplier")
	public void checkLaboratoryTest_Name(String name) {
		laboratoryTest.setName(name);
		
		var exp = validator.validate(laboratoryTest);
		
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
						obj.getMessage().contains("valid field can't be empty")||
						obj.getMessage().contains("valid length - 3 to 100 chars")
						)
				);		
	}
	
	private Stream<String> longStringSupplier(){
		return Stream.of(Strings.repeat("A", 101));
	}
	
	@Test
	public void checkLaboratoryTest_MaterialTypeNotNull() {
		
		laboratoryTest.setMaterialType(null);
		var exp = validator.validate(laboratoryTest);
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> obj.getMessage().contains("valid field can't be empty"))
				);
	}
	
	@Test
	public void checkLaboratoryTest_addMethod() {
		doAnswer(
				invocation -> {
					invocation
					.getArgument(0, LaboratoryTest.class)
					.getMethod()
					.add(method);
					return null;
					}).when(method).setLaboratoryTest(any(LaboratoryTest.class));
		laboratoryTest.addMethod(method);
		
		assertTrue(validator.validate(laboratoryTest).isEmpty());
		assertTrue(laboratoryTest.getMethod().contains(method));
	}
	
	@Test
	public void checkLaboratoryTest_removeMethod() {	
		doAnswer(
				invocation -> {
					laboratoryTest
					.getMethod()
					.remove(method);
					return null;
					}).when(method).setLaboratoryTest(isNull());
		laboratoryTest.getMethod().add(method);
		laboratoryTest.removeMethod(method);
		
		assertTrue(validator.validate(laboratoryTest).isEmpty());
		assertTrue(!laboratoryTest.getMethod().contains(method));
	}
}
