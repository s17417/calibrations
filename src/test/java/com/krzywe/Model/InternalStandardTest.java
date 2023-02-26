package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class InternalStandardTest {
	
	private InternalStandard internalStandard;
	
	private Validator validator;
	
	private static Random rand= new Random();
	
	private Supplier<String> aliasGenerator = () -> 
		rand
		.ints(97, 123)
		.limit(10)
		.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		.toString();
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		internalStandard = new InternalStandard();
		internalStandard.addAlias("acylcarnitine");
	}
	
	@Test
	public void checkCorrectInternalStandardValues() {
		assertTrue(() -> validator.validate(internalStandard).isEmpty());
	}
	
	@Test
	public void checkInternalStandard_Null() {
		internalStandard.removeAlias("acylcarnitine");
		var exp = validator.validate(internalStandard);
		assertTrue(!exp.isEmpty());
		System.out.println(exp);
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().equals("valid collection size - 1 up to 32 aliases")));
	}
	
	@ParameterizedTest
	@MethodSource("aliasListSupplier")
	public void  checkInternalStandardAliases(List<String> list) {
		internalStandard.setAliases(list);
		var exp = validator.validate(internalStandard);
		assertTrue(!exp.isEmpty());
	}
	
	private Stream<List<String>> aliasListSupplier() {
		return Stream.of(Stream
				.generate(aliasGenerator)
				.limit(33)
				.collect(Collectors.toList())
				);
	}
	
}
