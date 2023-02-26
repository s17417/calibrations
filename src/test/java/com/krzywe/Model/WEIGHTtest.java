package com.krzywe.Model;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WEIGHTtest {
	
	@ParameterizedTest
	@MethodSource("generateNonActiveResponseList")
	public void calculateWeigths_AllImplementation(Collection<ResponseValue> responseValues) {
		Arrays.stream(WEIGHT.values())
		.forEach(obj ->
			assertThat(obj.calculateWeights(responseValues))
			.isEmpty()
			);
	}
	
	@ParameterizedTest
	@MethodSource("generateActiveResponseList")
	public void calculateWeigths_UNWEIGHTED(Collection<ResponseValue> responseValues) {
		assertThat(WEIGHT.UNWEIGHTED.calculateWeights(responseValues))
		.hasSameSizeAs(responseValues)
		.allSatisfy((response, value) -> 
			value.compareTo(response.getResponseCalculationValue())
		);		
	}
	
	@ParameterizedTest
	@MethodSource("generateActiveResponseList")
	public void calculateWeigths_ONE_BY_X(Collection<ResponseValue> responseValues) {
		assertThat(WEIGHT.ONE_BY_X.calculateWeights(responseValues))
		.hasSameSizeAs(responseValues)
		.allSatisfy((response, value) -> 
			value.compareTo(BigDecimal.ONE.divide(response.getResponseCalculationValue(),32,RoundingMode.HALF_UP))
		);		
	}
	
	@ParameterizedTest
	@MethodSource("generateActiveResponseList")
	public void calculateWeigths_ONE_BY_X2(Collection<ResponseValue> responseValues) {
		assertThat(WEIGHT.ONE_BY_X2.calculateWeights(responseValues))
		.hasSameSizeAs(responseValues)
		.allSatisfy((response, value) -> 
			assertThat(value)
			.isEqualByComparingTo(
					BigDecimal.ONE.divide(response.getTargetConcentrationValue().pow(2),32,RoundingMode.HALF_UP)
					)
		);		
	}
	
	@ParameterizedTest
	@MethodSource("generateActiveResponseList")
	public void calculateWeigths_ONE_BY_Y(Collection<ResponseValue> responseValues) {
		assertThat(WEIGHT.ONE_BY_Y.calculateWeights(responseValues))
		.hasSameSizeAs(responseValues)
		.allSatisfy((response, value) -> 
			assertThat(value)
			.isEqualByComparingTo(
					BigDecimal.ONE.divide(response.getResponseCalculationValue(),32,RoundingMode.HALF_UP))
		);		
	}
	
	@ParameterizedTest
	@MethodSource("generateActiveResponseList")
	public void calculateWeigths_ONE_BY_Y2(Collection<ResponseValue> responseValues) {
		assertThat(WEIGHT.ONE_BY_Y2.calculateWeights(responseValues))
		.hasSameSizeAs(responseValues)
		.allSatisfy((response, value) -> 
		assertThat(value)
		.isEqualByComparingTo(
				BigDecimal.ONE.divide(response.getResponseCalculationValue().pow(2),32,RoundingMode.HALF_UP))
		);			
	}
	
	@ParameterizedTest
	@MethodSource("getDividableWeights")
	public void calculateWeigths_ONE_BY_X_divideByZero(WEIGHT weight) {
		assertThat(weight.calculateWeights(generateResponseList(
				new Double[] {0.0},
				new Double[] {0.0},
				new Boolean[] {true})))
		.allSatisfy((response, value) -> 
			assertThat(value)
			.isEqualByComparingTo(
					BigDecimal.ONE
					)
			);	
	}
	
	private Stream<List<ResponseValue>> generateActiveResponseList(){
		return Stream.of(
				generateResponseList(
						new Double[] {1.0,2.0,3.0,4.0,5.0},
						new Double[] {10.0,20.0,30.0,40.0,50.0},
						new Boolean[] {true,true,true,true,true}
						)
				);
	}
	
	private Stream<List<ResponseValue>> generateNonActiveResponseList(){
		return Stream.of(
				generateResponseList(
						new Double[] {1.0,2.0,3.0,4.0,5.0},
						new Double[] {10.0,20.0,30.0,40.0,50.0},
						new Boolean[] {false,false,false,false,false}
						),
				List.<ResponseValue>of()
				);
	}
	
	private List<ResponseValue> generateResponseList(Double[] arrResponse, Double[] arrTarget, Boolean[] isActive) {
		return IntStream
		.range(0, arrResponse.length)
		.mapToObj(n -> {
			var mock = mock(ResponseValue.class);
			when(mock.getResponseCalculationValue()).thenReturn(BigDecimal.valueOf(arrResponse[n]));
			when(mock.getTargetConcentrationValue()).thenReturn(BigDecimal.valueOf(arrTarget[n]));
			when(mock.getIsActive()).thenReturn(isActive[n]);
			return mock;
		}).collect(Collectors.toList());	
	}
	
	private Stream<WEIGHT> getDividableWeights(){
		return Stream.of(WEIGHT.values()
				);
	}

}
