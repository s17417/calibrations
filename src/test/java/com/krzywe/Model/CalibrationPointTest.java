package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.*;



@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalibrationPointTest {
	
	private Validator validator;
	
	@Mock
	private CalibrationSet calibrationSet;
	
	@Mock
	private TargetValue targetValue;
	
	private CalibrationPoint calibrationPoint;
	
	private static Random rand= new Random();
	
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
		when(calibrationSet.getCalibrationPoints()).thenReturn(new HashSet<CalibrationPoint>());
		calibrationPoint = new CalibrationPoint();
		calibrationPoint.setPointId("1/2022/09");
		calibrationPoint.setCalibrationSet(calibrationSet);
	}
	
	@Test
	public void checkCorrectCalibrationPointValues() {
		assertTrue(validator.validate(calibrationPoint).isEmpty());
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {" ", "12"})
	@MethodSource("longStringSupplier")
	public void checkCalibrationPointPointId(String pointId) {
		calibrationPoint.setPointId(pointId);
		var exp = validator.validate(calibrationPoint);
		assertTrue(
				exp
				.stream()
				.map(ConstraintViolation::getMessage)
				.anyMatch(obj -> 
					obj.contains("valid field can't be empty")||
					obj.contains("valid length - 3 to 32 chars")
					)
				);		
	}
	
	private Stream<String> longStringSupplier() {
		return Stream.of(Strings.repeat("aa", 33));
	}
	
	@Test
	public void checkCalibrationPoint_addAliases() {
		List<String> list = new ArrayList<>(Arrays.asList(new String[]{"phe","hhf","fff"}));
		calibrationPoint.setAliases(list);
		
		assertTrue(validator.validate(calibrationPoint).isEmpty());
		assertThat(calibrationPoint.getAliases())
		.containsExactly(
				list
				.stream()
				.map(String::toUpperCase)
				.collect(Collectors.toList())
				.toArray(new String[list.size()])
				);
	}
	
	@Test
	public void checkCalibrationPoint_removeAliases() {
		List<String> list = new ArrayList<>(Arrays.asList(new String[]{"phe","hhf","fff"}));
		calibrationPoint.setAliases(list);
		list.remove(0);
		calibrationPoint.removeAlias("PHE");
		
		assertTrue(validator.validate(calibrationPoint).isEmpty());
		assertThat(calibrationPoint.getAliases())
		.containsExactly(
				list
				.stream()
				.map(String::toUpperCase)
				.toArray(String[]::new)
				);
	}
	
	@ParameterizedTest
	@ArgumentsSource(ArgumentsSupplier.class)
	public void checkCalibrationPoint_AliasesConstraints(List<String> list) {
		calibrationPoint.setAliases(list);
		
		var exp = validator.validate(calibrationPoint);
		
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
	public void checkCalibrationPoint_CalibrationSetNotNull() {
		calibrationPoint.setCalibrationSet(null);
		
		var exp = validator.validate(calibrationPoint);
		
		assertTrue(exp.size()==1);
		assertTrue(
				exp
				.stream()
				.map(obj -> obj.getMessage())
				.anyMatch(obj -> obj.contains("valid field can't be empty"))
				);
	}
	
	@Test
	public void checkCalibrationPoint_addTargetValue() {
		doAnswer(invocation -> {
			invocation
			.getArgument(0, CalibrationPoint.class)
			.getTargetValues()
			.add(targetValue);
		return null;	
		}).when(targetValue).setCalibrationPoint(any(CalibrationPoint.class));
		calibrationPoint.addTargetValue(targetValue);
		
		assertTrue(validator.validate(calibrationPoint).isEmpty());	
		assertTrue(calibrationPoint.getTargetValues().contains(targetValue));
	}
	
	@Test
	public void checkCalibrationPoint_removeTargetValue() {
		doAnswer(invocation -> {
			calibrationPoint
			.getTargetValues()
			.remove(targetValue);
		return null;	
		}).when(targetValue).setCalibrationPoint(isNull());
		calibrationPoint.getTargetValues().add(targetValue);
		calibrationPoint.removeTargetValue(targetValue);
		
		assertTrue(validator.validate(calibrationPoint).isEmpty());	
		assertTrue(!calibrationPoint.getTargetValues().contains(targetValue));
	}
	
	
}
