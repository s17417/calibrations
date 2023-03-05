package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;

import com.krzywe.Utils.Exceptions.CalculationResponseValueStrategyNullPointerException;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ResponseValueTest {
	
	private ResponseValue responseValue;
	
	private Validator validator;
	
	@Mock
	private CalculatedCalibrationCurve calculatedCalibrationCurve;
	
	@Mock
	private TargetValue targetValue;
	
	@Mock
	private ICalculationResponseValueStrategy calculationResponseValueStrategy;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		responseValue = new ResponseValue();
		responseValue.setTargetValue(targetValue);
		responseValue.setCalculatedCalibrationCurve(calculatedCalibrationCurve);
		responseValue.setCalculationResponseValueStrategy(calculationResponseValueStrategy);
	}
	
	@Test
	public void checkCorrectResponseValueValues() {
		assertTrue(validator.validate(responseValue).isEmpty());
	}
	
	@Test
	public void checkResponseValueTargetvalue_NotNull() {
		responseValue.setTargetValue(null);
		var exp = validator.validate(responseValue);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
	}
	
	@Test
	public void checkResponseValueCalculatedCalibrationCurve_NotNull() {
		when(calculatedCalibrationCurve.getResponseValues())
		.thenReturn(Sets.newSet(responseValue));
		
		responseValue.setCalculatedCalibrationCurve(null);
		var exp = validator.validate(responseValue);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("valid field can't be empty")));
	}
	
	@ParameterizedTest
	@NullSource
	@ValueSource(doubles = {-1,1.1234567890123,123456789012345678901D})
	public void checkResponseValue_responseValue(Double value) {
		responseValue.setResponseValue(value!=null?BigDecimal.valueOf(value):null);
		var exp = validator.validate(responseValue);
		assertTrue(!exp.isEmpty());
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
				obj.getMessage().contains("valid field can't be empty")||
				obj.getMessage().contains("valid number must be equal or greater than 0")||
				obj.getMessage().contains("numeric value out of bounds (<20 digits>.<12 digits> expected)")
				)
				);
	}
	
	@ParameterizedTest
	@ValueSource(doubles = {-1,0,1.1234567890123,123456789012345678901D})
	public void checkResponseValue_internalStdResponseValue(Double value) {
		responseValue.setInternalStdResponseValue(BigDecimal.valueOf(value));
		var exp = validator.validate(responseValue);
		assertTrue(!exp.isEmpty());
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
				obj.getMessage().contains("valid field can't be empty")||
				obj.getMessage().contains("valid number must greater than 0")||
				obj.getMessage().contains("numeric value out of bounds (<20 digits>.<12 digits> expected)")
				)
				);
	}
	
	@ParameterizedTest
	@ValueSource(doubles = {-1,0,1.1234567890123,123456789012345678901D})
	public void checkResponseValue_internalStdConcentration(Double value) {
		responseValue.setInternalStdConcentration(BigDecimal.valueOf(value));
		var exp = validator.validate(responseValue);
		assertTrue(!exp.isEmpty());
		assertTrue(
				exp
				.stream()
				.anyMatch(obj -> 
				obj.getMessage().contains("valid field can't be empty")||
				obj.getMessage().contains("valid number must greater than 0")||
				obj.getMessage().contains("numeric value out of bounds (<20 digits>.<12 digits> expected)")
				)
				);
	}
	
	@Test
	public void checkResponseValue_getResponseCalculationValue() {
		when(calculationResponseValueStrategy.getResponseCalculationValue(any(BigDecimal.class),isNull()))
		.thenReturn(BigDecimal.ONE);
		assertTrue(responseValue.getResponseCalculationValue().compareTo(BigDecimal.ONE)==0);
	}
	
	@Test
	public void checkResponseValue_getTargetConcentrationValue() {
		when(targetValue.getTargetValue()).thenReturn(BigDecimal.ONE);
		when(calculationResponseValueStrategy.getTargetConcentrationValue(any(BigDecimal.class), isNull()))
		.thenReturn(BigDecimal.ONE);
		assertTrue(responseValue.getTargetConcentrationValue().compareTo(BigDecimal.ONE)==0);
	}
	
	@Test
	public void checkResponseValueGetResponseCalculationValue_NullStrategy() {
		responseValue.setCalculationResponseValueStrategy(null);
		assertThrows(CalculationResponseValueStrategyNullPointerException.class,() -> responseValue.getResponseCalculationValue());
	}
	
	@Test
	public void checkResponseValueGetTargetConcentrationValue_NullStrategy() {
		responseValue.setCalculationResponseValueStrategy(null);
		assertThrows(CalculationResponseValueStrategyNullPointerException.class,() -> responseValue.getTargetConcentrationValue());
	}

}
