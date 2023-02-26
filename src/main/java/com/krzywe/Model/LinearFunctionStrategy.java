package com.krzywe.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class LinearFunctionStrategy implements IFunctionStrategy {

	@Override
	public List<BigDecimal> calculateParameters(Collection<ResponseValue> responseValues, WEIGHT weight) {
		return IFunctionStrategy.super.calculateParameters(responseValues, weight);
	}
	
	@Override
	public Optional<BigDecimal> calculateCoefficientOfDetermination(Collection<ResponseValue> responseValues, Collection<BigDecimal> parameters) {
		return IFunctionStrategy.super.calculateCoefficientOfDetermination(responseValues, parameters);
	}

	@Override
	public Optional<String> getFormula(List<BigDecimal> parameters) {
		
		var list = IntStream
		.range(0, parameters.size())
		.mapToObj(i -> 
				parameters.get(i).compareTo(BigDecimal.ZERO)==0 ? 
						"" : parameters.get(i) + (i!=0 ? "x" : "") + (i>1 ? i : "")
				)
		.filter(element -> !element.isEmpty())
		.collect(Collectors.toList());
		
		Collections.reverse(list);
		
		return list.stream()
		.reduce((partialString, element) -> partialString+(element.contains("-") ? "" : "+")+element);
	}

	@Override
	public List<BigDecimal> setParameters(List<BigDecimal> parameters) {
		if (!parameters.isEmpty()) 
			return parameters.size()<2 ? parameters.subList(0, 1) : parameters.subList(0, 2);
		else return new ArrayList<>();
	}

}
