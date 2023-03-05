package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;



import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validation;
import jakarta.validation.Validator;


@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AnalyteTest {
	
	private Analyte analyte;
	
	@Mock
	private Method method;
	
	private Validator validator;
	
	private static Random rand = new Random();
	
	private static Supplier<String> stringSupplier = () -> 
	rand
	.ints(97,123)
	.limit(4)
	.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	.toString();
	
	private static class ArgumentsSupplier implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
			return Stream.of(
					Arguments.of(
			        		  Stream
			        		  .generate(stringSupplier)
			        		  .limit(36)
			        		  .collect(Collectors.toList())
			        		  ), 
			          Arguments.of(List.of("aa")), 
			          Arguments.of(List.of(Strings.repeat("a", 33))) 
					);
		}
		
	}
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();

	}
	
	@BeforeEach
	public void beforeEach() {
		analyte = new Analyte();
		analyte.setName("Citrulline");
	}
	
	@Test
	public void checkCorrectAnalyte_Values() {
		assertTrue(validator.validate(analyte).isEmpty());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"1",""})
	@MethodSource("longStringSupplier")
	public void checkAnalyte_Name(String name) {
		analyte.setName(name);
		
		var exp = validator.validate(analyte);
		System.out.println(exp);
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
						obj.getMessage().contains("valid field can't be empty")||
						obj.getMessage().contains("valid length - 3 to 100 chars")
						)
				);
		}
	
	private Stream<String> longStringSupplier() {
		return Stream.of(Strings.repeat("qa", 51));
	}
	
	@Test
	public void checkAnalyte_addAliases() {
		var list = Lists.list("phe","hpa","pku");
		analyte.setAliases(list);
		
		assertTrue(validator.validate(analyte).isEmpty());
		assertThat(analyte.getAliases()).containsExactly(
				list
				.stream()
				.map(obj -> obj.toUpperCase())
				.toArray(String[]::new)
				);	
	}
	
	public void checkAnalyte_removeAliases() {
		var list = Lists.list("phe","hpa","pku");
		analyte.setAliases(list);
		
		analyte.removeAlias(list.remove(0));
		
		assertTrue(validator.validate(analyte).isEmpty());
		assertThat(analyte.getAliases()).containsExactly(
				list
				.stream()
				.map(obj -> obj.toUpperCase())
				.toArray(String[]::new)
				);	
	}
	
	@ParameterizedTest
	@ArgumentsSource(ArgumentsSupplier.class)
	public void checkCalibrationPoint_AliasesConstraints(List<String> list) {
		analyte.setAliases(list);
		
		var exp = validator.validate(analyte);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(obj -> obj.getMessage())
				.anyMatch(obj ->
				obj.contains("valid collection size - up to 32 aliases")||
				obj.contains("valid length - 3 to 32 chars")
				)
		);
	}
	
	@Test
	public void checkAnalyte_addMethod() {
		doAnswer(
				invocation -> {
					invocation
					.getArgument(0, Analyte.class)
					.getMethod()
					.add(method);
					return null;
					}).when(method).setAnalyte(any(Analyte.class));
		analyte.addMethod(method);
		
		assertTrue(validator.validate(analyte).isEmpty());
		assertTrue(analyte.getMethod().contains(method));
	}
	
	@Test
	public void checkAnalyte_removeMethod() {	
		doAnswer(
				invocation -> {
					analyte
					.getMethod()
					.remove(method);
					return null;
					}).when(method).setAnalyte(isNull());
		analyte.getMethod().add(method);
		analyte.removeMethod(method);
		
		assertTrue(validator.validate(analyte).isEmpty());
		assertTrue(!analyte.getMethod().contains(method));
	}
}
