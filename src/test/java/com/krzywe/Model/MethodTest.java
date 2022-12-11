package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.stream.Stream;

import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MethodTest {
	
	private Method method;
	
	@Mock
	private Analyte analyte;
	
	@Mock
	private LaboratoryTest laboratoryTest;
	
	private Validator validator;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		when(analyte.getMethod()).thenReturn(new HashSet<Method>());
		method = new Method();
		method.setResponseValueUnits("L");
		method.setAnalyticalMethod("UVVIS");
		method.setAnalyte(analyte);
		method.setLaboratoryTest(laboratoryTest);
	}
	
	@Test
	public void checkCorrectMethodValues() {
		assertTrue(validator.validate(method).isEmpty());
	}
	
	@Test
	public void checkResponseValueUnits_Nullable() {
		method.setResponseValueUnits(null);
		assertTrue(validator.validate(method).isEmpty());
	}
	
	@Test
	public void checkResponseValueUnits_MazSize() {
		method.setResponseValueUnits(Strings.repeat("La", 51));
		var exp = validator.validate(method);
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
						obj.getMessage().contains("valid length - up to 100 chars")
						)
				);
		
	}
	
	@Test
	public void checkMethod_AnalyticalMethodNullable() {
		method.setAnalyticalMethod(null);
		assertTrue(validator.validate(method).isEmpty());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"LA"})
	@MethodSource("longStringSupplier")
	public void checkMethod_AnalyticalMethodSizeConstraints(String analyticalMethod) {
		method.setAnalyticalMethod(analyticalMethod);
		var exp = validator.validate(method);
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("valid length - 3 to 100 chars")));
	}
	
	private Stream<String> longStringSupplier(){
		return Stream.of(Strings.repeat("La", 51));
	}
	
	@Test
	public void checkMethod_AnalyteNotNull() {
		method.setAnalyte(null);
		
		var exp = validator.validate(method);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(obj -> obj.getMessage())
				.anyMatch(obj -> obj.contains("valid field can't be empty"))
				);
	}
	
	@Test
	public void checkMethod_SetAnalyte() {
		method.setAnalyte(null);
		method.setAnalyte(analyte);
		
		assertTrue(validator.validate(method).isEmpty());
		assertTrue(method.getAnalyte().equals(analyte));
	}
	
	@Test
	public void checkMethod_LaboratoryTestNotNull() {
		method.setLaboratoryTest(null);
		
		var exp = validator.validate(method);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(obj -> obj.getMessage())
				.anyMatch(obj -> obj.contains("valid field can't be empty"))
				);
	}
	
	@Test
	public void checkMethod_SetLaboratoryTest() {
		method.setLaboratoryTest(null);
		method.setLaboratoryTest(laboratoryTest);
		
		assertTrue(validator.validate(method).isEmpty());
		assertTrue(method.getAnalyte().equals(analyte));
	}

}
