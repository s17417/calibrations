package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LinearFunctionStrategyTest {
	
	private LinearFunctionStrategy linearFunctionStrategy = new LinearFunctionStrategy();

	@Test
	public void checkLinearFunctionStrategy_calculateParameters() {
		//function 10x+10
		var list = new ArrayList<Double[]>();
		for (Double i=0D; i<5;i++)
			list.add(new Double[] {i*10+10,i,1D});
		var result=linearFunctionStrategy.calculateParameters(pointsGenerator(list), WEIGHT.UNWEIGHTED);
		assertThat(result)
		.elements(0,1)
		.usingElementComparator(BigDecimal::compareTo)
		.containsExactly(BigDecimal.TEN, BigDecimal.TEN);
		
		//function 4x+1
		list.clear();
		for (Double i=0D; i<30; i++)
			list.add(new Double[]{i*4+1,i,1D});
		result = linearFunctionStrategy.calculateParameters(pointsGenerator(list), WEIGHT.UNWEIGHTED);
		assertThat(result)
		.elements(0,1)
		.usingElementComparator(BigDecimal::compareTo)
		.containsExactly(BigDecimal.ONE, BigDecimal.valueOf(4));
	
	}
	
	@Test
	public void checkLinearFunctionStrategyCalculateParameters_EmptyList() {
		var list = new ArrayList<ResponseValue>();
		
		assertThat(linearFunctionStrategy.calculateParameters(list, WEIGHT.UNWEIGHTED))
		.isEmpty();
	}
	
	@Test
	public void checkLinearFunctionStrategyCalculateParameters_NotActiveResponseValues() {
		var list = new ArrayList<Double[]>();
		for (Double i=0D; i<5; i++)
			list.add(new Double[] {i*2,i,0D});
		assertThat(linearFunctionStrategy.calculateParameters(pointsGenerator(list), WEIGHT.UNWEIGHTED))
		.isEmpty();
	}
	
	@Test
	public void checkLinearFunctionStrategyCalculateParameters_SeveralNotActiveResponseValues() {
		//function 2x
		var list = new ArrayList<Double[]>();
		for (Double i=0D; i<5; i++)
			list.add(new Double[] {i*2,i,1D});
		list.add(new Double[] {5D,3D,0D});
		list.add(new Double[] {10D,9D,0D});
		var result = linearFunctionStrategy.calculateParameters(pointsGenerator(list), WEIGHT.UNWEIGHTED);	
		
		assertThat(result)
		.elements(0,1)
		.usingElementComparator(BigDecimal::compareTo)
		.containsExactly(BigDecimal.ZERO,BigDecimal.valueOf(2));
	}
	
	@Test
	public void checkLinearFunctionStrategy_calculateCoefficientOfDetermination() {
		var list = new ArrayList<Double[]>();
		//coefficient 1.000
		for (Double i=0D; i<5;i++)
			list.add(new Double[] {i*1+10,i,1D});
		var pointList = pointsGenerator(list);
		var parameters = linearFunctionStrategy.calculateParameters(pointList, WEIGHT.UNWEIGHTED);
		
		var result = linearFunctionStrategy.calculateCoefficientOfDetermination(pointList, parameters);
		assertThat(result)
		.isPresent()
		.get()
		.usingComparator(BigDecimal::compareTo)
		.isEqualTo(BigDecimal.ONE);
	
		//coefficient 0.5
		list.clear();
		for (Double i=0D; i<5;i++)
			for(Double j=0D; j<5;j++)
			list.add(new Double[] {j+i,i,1D});
		pointList = pointsGenerator(list);
		parameters = linearFunctionStrategy.calculateParameters(pointList, WEIGHT.UNWEIGHTED);
		
		 result = linearFunctionStrategy.calculateCoefficientOfDetermination(pointList, parameters);
		assertThat(result)
		.isPresent()
		.get()
		.usingComparator(BigDecimal::compareTo)
		.isEqualTo(BigDecimal.valueOf(0.5));
	}
	
	@Test
	public void checkLinearFunctionStrategyCalculateCoefficientOfDetermination_EmptyList() {
		var responseList = List.<ResponseValue>of();
		var parameters = List.<BigDecimal>of();
		
		var result = linearFunctionStrategy.calculateCoefficientOfDetermination(responseList, parameters);
		assertThat(result).isEmpty();
		
		parameters = List.of(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
		
		result = linearFunctionStrategy.calculateCoefficientOfDetermination(responseList, parameters);
		assertThat(result).isEmpty();
	}
	
	@Test
	public void checkLinearFunctionStrategyCalculateCoefficientOfDetermination_NotActiveList() {
		var list = new ArrayList<Double[]>();
		for (Double i=0D; i<5;i++)
			list.add(new Double[] {i*1+10,i,0D});
		var pointList = pointsGenerator(list);
		var parameters = linearFunctionStrategy.calculateParameters(pointList, WEIGHT.UNWEIGHTED);
		
		var result = linearFunctionStrategy.calculateCoefficientOfDetermination(pointList, parameters);
		assertThat(result).isEmpty();
		
		parameters = List.of(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
		
		result = linearFunctionStrategy.calculateCoefficientOfDetermination(pointList, parameters);
		assertThat(result).isEmpty();
	}
	
	@Test
	public void checkLinearFunctionStrategy_Formula() {
		//formula 10x+1
		assertThat(linearFunctionStrategy.getFormula(List.of(BigDecimal.ONE, BigDecimal.TEN)))
		.isPresent()
		.hasValueSatisfying(obj -> obj.contains("10x+1"));
		
		//formula 10x-1
		assertThat(linearFunctionStrategy.getFormula(List.of(BigDecimal.valueOf(-1), BigDecimal.TEN)))
		.isPresent()
		.hasValueSatisfying(obj -> obj.contains("10x-1"));
		
		//formula -10x+1
		assertThat(linearFunctionStrategy.getFormula(List.of(BigDecimal.ONE, BigDecimal.valueOf(-10))))
		.isPresent()
		.hasValueSatisfying(obj -> obj.contains("-10x+1"));
		
		//formula 10x
		assertThat(linearFunctionStrategy.getFormula(List.of(BigDecimal.ZERO, BigDecimal.TEN)))
		.isPresent()
		.hasValueSatisfying(obj -> obj.contains("10x"));	
	}
	
	@Test
	public void checkLinearFunctionStrategyFormula_emptyParameters() {
		assertThat(linearFunctionStrategy.getFormula(List.of()))
		.isEmpty();
	}
	
	@Test
	public void checkLinearFunctionStrategy_setParameters() {
		assertThat(linearFunctionStrategy.setParameters(List.of(BigDecimal.ONE, BigDecimal.TEN)))
		.elements(0,1)
		.usingElementComparator((obj1, obj2) -> obj1.compareTo(obj2))
		.containsExactly(BigDecimal.ONE,BigDecimal.TEN);
	}
	
	@Test
	public void checkLinearFunctionStrategySetParameters_NotSameSize() {
		//three elements
		assertThat(linearFunctionStrategy.setParameters(List.of(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO)))
		.hasSize(2)
		.elements(0,1)
		.usingElementComparator(BigDecimal::compareTo)
		.containsExactly(BigDecimal.ONE,BigDecimal.TEN);
		
		//two elements
		assertThat(linearFunctionStrategy.setParameters(List.of(BigDecimal.ONE)))
		.hasSize(1)
		.elements(0)
		.usingElementComparator((BigDecimal::compareTo))
		.containsExactly(BigDecimal.ONE);
	}
	
	@Test
	public void checkLinearFunctionStrategySetParameters_EmptyList() {
		assertThat(linearFunctionStrategy.setParameters(List.of()))
		.hasSize(0);
	}
	
	private List<ResponseValue> pointsGenerator(List<Double[]> arr) {
		var mockedListResponse = Stream
				.generate(() -> mock(ResponseValue.class))
				.limit(arr.size())
				.collect(Collectors.toList());
				
				for (int i=0; i<arr.size();i++) {
					when(mockedListResponse.get(i).getIsActive()).thenReturn(arr.get(i)[2]==1?true:false);
					when(mockedListResponse.get(i).getResponseCalculationValue()).thenReturn(BigDecimal.valueOf(arr.get(i)[0]));
					when(mockedListResponse.get(i).getTargetConcentrationValue()).thenReturn(BigDecimal.valueOf(arr.get(i)[1]));
				}
	return mockedListResponse;			
	
	}
}
