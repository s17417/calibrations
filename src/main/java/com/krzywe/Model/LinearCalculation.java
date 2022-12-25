package com.krzywe.Model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.bytebuddy.implementation.MethodCall.ArgumentLoader.ForMethodParameterArray;


public class LinearCalculation implements ICalculateEquation {

	@Override
	public List<BigDecimal> calculateParameters(Collection<ResponseValue> responseValues) {
		BigDecimal xSum = BigDecimal.ZERO, 
				ySum = BigDecimal.ZERO,
				xSquaredSum = BigDecimal.ZERO,
				xySum= BigDecimal.ZERO,
				beta0,
				beta1;
		int n=0;
		for (ResponseValue value: responseValues) {
			if (!value.getIsActive()) continue;
			n += 1;
			xSum = xSum.add(
					value.getTargetValue().
					getTargetValue()
					);
			xSquaredSum = xSquaredSum.add(
					value.getTargetValue()
					.getTargetValue()
					.multiply(value.getTargetValue().getTargetValue(), new MathContext(22, RoundingMode.HALF_UP))
					);
			ySum = ySum.add(
					value.getResponseValue()
					);
			xySum = xySum.add(
					value.getTargetValue()
					.getTargetValue()
					.multiply(value.getResponseValue(), new MathContext(22, RoundingMode.HALF_UP))
					);
		
		}
		
		if (n == 0) return List.of();
		var xyS = xySum.subtract(
				xSum
				.multiply(ySum)
				.divide(BigDecimal.valueOf(n),22,RoundingMode.HALF_UP)
				);
		var xxS = xSquaredSum.subtract(
				xSum
				.multiply(xSum)
				.divide(BigDecimal.valueOf(n),22,RoundingMode.HALF_UP)
				);
		
		if (xxS.compareTo(BigDecimal.ZERO)==0) return List.of();
		beta1 = xyS
				.divide(xxS,22,RoundingMode.HALF_UP)
				.setScale(20, RoundingMode.HALF_UP);
		beta0 = ySum
				.divide(BigDecimal.valueOf(n),22,RoundingMode.HALF_UP)
				.subtract(
						beta1.multiply(
								xSum.divide(BigDecimal.valueOf(n),22,RoundingMode.HALF_UP)
								)
						)
				.setScale(20, RoundingMode.HALF_UP);
		return List.of(
				beta0,
				beta1
				);
	}
	
	

	@Override
	public Optional<BigDecimal> calculateCoefficientOfDetermination(Collection<ResponseValue> responseValues, Collection<BigDecimal> parameters) {
		BigDecimal ySum = BigDecimal.ZERO,
				ySquare = BigDecimal.ZERO,
				xySum = BigDecimal.ZERO;
		int n = 0;
		
		for(ResponseValue responseValue : responseValues) {
			
			if(!responseValue.getIsActive()) continue;
			n += 1;
			
			ySum = ySum.add(responseValue.getResponseValue());
			ySquare = ySquare.add(
					responseValue.getResponseValue()
					.multiply(responseValue.getResponseValue(), new MathContext(22, RoundingMode.HALF_UP))
					);
			xySum = xySum.add(
					responseValue.getResponseValue()
					.multiply(responseValue.getTargetValue().getTargetValue(), new MathContext(22, RoundingMode.HALF_UP))
					);				
		}
		
		if (parameters.isEmpty()) return Optional.empty();
		var paramArray = parameters.toArray(new BigDecimal[2]);
		var sse = ySquare
				.subtract(paramArray[0].multiply(ySum), new MathContext(22, RoundingMode.HALF_UP))
				.subtract(paramArray[1].multiply(xySum), new MathContext(22, RoundingMode.HALF_UP));
		
		var sst = ySquare
				.subtract(
						ySum.multiply(ySum, new MathContext(22, RoundingMode.HALF_UP))
						.divide(BigDecimal.valueOf(n))
						);
		
		return Optional.of(BigDecimal.ONE
				.subtract(sse.divide(sst, new MathContext(20, RoundingMode.HALF_UP)))
				.setScale(20, RoundingMode.HALF_UP));
	}



	@Override
	public Optional<String> getFormula(List<BigDecimal> parameters) {
		
		var list = IntStream
		.range(0, parameters.size())
		.mapToObj(i -> 
				parameters.get(i).compareTo(BigDecimal.ZERO)==0 ? 
						"" : parameters.get(i).stripTrailingZeros() + (i!=0 ? "x" : "") + (i>1 ? i : "")
				)
		.filter(element -> !element.isEmpty())
		.collect(Collectors.toList());
		
		Collections.reverse(list);
		
		return list.stream()
		.reduce((partialString, element) -> partialString+(element.contains("-") ? "" : "+")+element);
	}

	@Override
	public Integer getMaximalParametersSize() {
		return 2;
	}

	@Override
	public List<BigDecimal> setParameters(List<BigDecimal> parameters) {
		if (!parameters.isEmpty()) 
			return parameters.size()<2 ? parameters.subList(0, 0) : parameters.subList(0, 1);
		else return new ArrayList<>();
	}

}
