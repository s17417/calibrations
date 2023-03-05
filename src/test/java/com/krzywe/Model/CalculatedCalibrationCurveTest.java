package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class CalculatedCalibrationCurveTest {
	
	private CalculatedCalibrationCurve calculatedCalibrationCurve;
	
	@Mock
	private LinearFunctionStrategy linearFunctionStrategy;
	
	@Mock
	private ICalculationResponseValueStrategy calculationResponseValueStrategy;
	
	@Mock
	private ResponseValue responseValue;
	
	private Validator validator;
	
	@Mock
	private CalibrationCurve calibrationCurve;
	
	@BeforeAll
	public void beforeAll() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		calculatedCalibrationCurve = new CalculatedCalibrationCurve();
		calculatedCalibrationCurve.setCalibrationCurve(calibrationCurve);
	}
	
	@Test
	public void checkCorrectCalculatedCalibrationTypeValues() {
		assertTrue(validator.validate(calculatedCalibrationCurve).isEmpty());
	}
	
	@Test
	public void checkDefaultValues() {
		assertTrue(calculatedCalibrationCurve.getWeight()!=null);
		assertTrue(calculatedCalibrationCurve.getiCalculateEquation()!=null);
		assertTrue(calculatedCalibrationCurve.getCalculationResponseValueStrategy()!=null);
	}
	
	@Test
	public void checkCalculatedCalibrationCurveCoefficientOfDetermination_digitsScale() {
		calculatedCalibrationCurve.setCoefficientOfDetermination(new BigDecimal("0.99999"));
		var exp = validator.validate(calculatedCalibrationCurve);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("numeric value out of bounds (<1 digits>.<4 digits> expected)")));
		
		calculatedCalibrationCurve.setCoefficientOfDetermination(new BigDecimal("1.99999"));
		exp = validator.validate(calculatedCalibrationCurve);
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("numeric value out of bounds (<1 digits>.<4 digits> expected)")));
		assertTrue(!exp.isEmpty());
	}
	
	@Test
	public void checkCalculatedCalibrationCurveCoefficientOfDetermination_range() {
		calculatedCalibrationCurve.setCoefficientOfDetermination(new BigDecimal("1.9"));
		var exp = validator.validate(calculatedCalibrationCurve);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("valid number must be equal or smaller than 1")));
		
		calculatedCalibrationCurve.setCoefficientOfDetermination(new BigDecimal("-0.1"));
		exp = validator.validate(calculatedCalibrationCurve);
		assertTrue(!exp.isEmpty());
		assertTrue(exp.stream().anyMatch(obj -> obj.getMessage().contains("valid number must be equal or greater than 0")));
	}
	
	@Test
	public void checkCalculatedCalibrationCurveParameters_Order() {
		var list = Lists.list(new BigDecimal(1), new BigDecimal(2),new BigDecimal(3));
		calculatedCalibrationCurve.setParameters(Lists.list(new BigDecimal(1), new BigDecimal(2),new BigDecimal(3)));
		boolean order = true;
		for (int i=0; i<calculatedCalibrationCurve.getParameters().size(); i++) {
			if (calculatedCalibrationCurve.getParameters().get(i).compareTo(list.get(i))!=0){
			order=false;
			}
		}
		assertTrue(order);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void checkCalculatedCalibrationCurve_setParameters() {
		when(linearFunctionStrategy.setParameters(any(List.class)))
		.thenReturn(Lists.list(BigDecimal.valueOf(20.1)));
		
		calculatedCalibrationCurve.setiCalculateEquation(linearFunctionStrategy);
		calculatedCalibrationCurve.setParameters(Lists.newArrayList());
		assertTrue(
				calculatedCalibrationCurve
				.getParameters()
				.get(0)
				.compareTo(BigDecimal.valueOf(20.1))==0
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void checkCalculatedCalibrationCurve_getEquation() {
		when(linearFunctionStrategy.getFormula(any(List.class)))
		.thenReturn(Optional.of("Equation"));
		
		calculatedCalibrationCurve.setiCalculateEquation(linearFunctionStrategy);
		assertTrue(
				calculatedCalibrationCurve
				.getEquation()
				.get()
				.contentEquals("Equation")
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void checkCalculatedCalibrationCurve_calculateParameters() {
		when(linearFunctionStrategy.calculateParameters(any(Set.class), any(WEIGHT.class)))
		.thenReturn(Lists.list(BigDecimal.ONE));
		
		calculatedCalibrationCurve.setiCalculateEquation(linearFunctionStrategy);
		calculatedCalibrationCurve.setCalculationResponseValueStrategy(calculationResponseValueStrategy);
		assertTrue(
				calculatedCalibrationCurve
				.calculateParameters()
				.get(0)
				.compareTo(BigDecimal.ONE)==0
				);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void checkCalculatedCalibrationCurve_calculateCoefficientOfDetermination() {
		when(linearFunctionStrategy.calculateCoefficientOfDetermination(any(Set.class), any(List.class)))
		.thenReturn(Optional.of(BigDecimal.ONE));
		
		calculatedCalibrationCurve.setiCalculateEquation(linearFunctionStrategy);
		assertTrue(
				calculatedCalibrationCurve.calculateCoefficientOfDetermination()
				.get().compareTo(BigDecimal.ONE)==0
				);
	}
	
	@Test
	public void checkCalculatedCalibrationCurve_addResponseValue() {
		doAnswer(invocation -> {
			invocation
			.getArgument(0, CalculatedCalibrationCurve.class)
			.getResponseValues()
			.add(responseValue);
			return null;
		}
		).when(responseValue).setCalculatedCalibrationCurve(any(CalculatedCalibrationCurve.class));
		
		calculatedCalibrationCurve.addResponseValue(responseValue);
		assertTrue(!calculatedCalibrationCurve.getResponseValues().isEmpty());
		assertTrue(calculatedCalibrationCurve.getResponseValues().contains(responseValue));
	}
	
	@Test
	public void checkCalculatedCalibrationCurve_removeResponseValue() {
		doAnswer(invocation -> {
			calculatedCalibrationCurve
			.getResponseValues()
			.remove(responseValue);
			return null;
		}).when(responseValue).setCalculatedCalibrationCurve(isNull());
		
		calculatedCalibrationCurve.getResponseValues().add(responseValue);
		calculatedCalibrationCurve.removeResponseValue(responseValue);
		
		assertTrue(!calculatedCalibrationCurve.getResponseValues().contains(responseValue));
		assertTrue(calculatedCalibrationCurve.getResponseValues().isEmpty());
		
	}
	
	
}
