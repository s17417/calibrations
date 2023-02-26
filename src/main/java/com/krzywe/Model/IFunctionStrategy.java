package com.krzywe.Model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IFunctionStrategy {
	
	default public List<BigDecimal> calculateParameters(Collection<ResponseValue> responseValues, WEIGHT weight){
		var weightMap = weight.calculateWeights(responseValues);
		
		BigDecimal xSum = BigDecimal.ZERO, 
				ySum = BigDecimal.ZERO,
				xSquaredSum = BigDecimal.ZERO,
				xySum = BigDecimal.ZERO,
				wSum = BigDecimal.ZERO,
				beta0,
				beta1;
		int n=0;
		for (ResponseValue value: responseValues) {
			if (!value.getIsActive()) continue;
			n += 1;
			var responseWeight = weightMap.get(value);
			wSum = wSum.add(responseWeight);
			xSum = xSum.add(
					value.getTargetConcentrationValue().multiply(responseWeight)
					);
			xSquaredSum = xSquaredSum.add(
					value.getTargetConcentrationValue()
					.multiply(value.getTargetConcentrationValue(), new MathContext(22, RoundingMode.HALF_UP))
					.multiply(responseWeight)
					);
			ySum = ySum.add(
					value.getResponseCalculationValue().multiply(responseWeight)
					);
			xySum = xySum.add(
					value.getTargetConcentrationValue()
					.multiply(value.getResponseCalculationValue(), new MathContext(22, RoundingMode.HALF_UP))
					.multiply(responseWeight)
					);
		
		}
		
		if (n == 0) return List.of();
		var xyS = xySum.subtract(
				xSum
				.multiply(ySum)
				.divide(wSum,22,RoundingMode.HALF_UP)
				);
		var xxS = xSquaredSum.subtract(
				xSum
				.pow(2)
				.divide(wSum,22,RoundingMode.HALF_UP)
				);
		
		if (xxS.compareTo(BigDecimal.ZERO)==0) return List.of();
		beta1 = xyS
				.divide(xxS,22,RoundingMode.HALF_UP)
				.setScale(12, RoundingMode.HALF_UP);
		beta0 = ySum.divide(wSum,22,RoundingMode.HALF_UP)
				.subtract(beta1.multiply(xSum.divide(wSum/*BigDecimal.valueOf(n)*/,20,RoundingMode.HALF_UP)))
				
				.setScale(12, RoundingMode.HALF_UP);
		return List.of(
				beta0,
				beta1
				);
	}
	
	default public Optional<BigDecimal> calculateCoefficientOfDetermination(Collection<ResponseValue> responseValues, Collection<BigDecimal> parameters){
		if (parameters.isEmpty()) return Optional.empty();
		
		BigDecimal ySum = BigDecimal.ZERO,
				ySquare = BigDecimal.ZERO,
				xySum = BigDecimal.ZERO,
				sse = BigDecimal.ZERO;
		int n = 0;
		var paramArray = parameters.toArray(new BigDecimal[2]);
		
		
		for(ResponseValue responseValue : responseValues) {
			
			if(!responseValue.getIsActive()) continue;
			n += 1;
			
			ySum = ySum.add(responseValue.getResponseCalculationValue());
			ySquare = ySquare.add(
					responseValue.getResponseCalculationValue()
					.pow(2)
					);
			xySum = xySum.add(
					responseValue.getResponseCalculationValue()
					.multiply(responseValue.getTargetConcentrationValue(), new MathContext(22, RoundingMode.HALF_UP))
					);	
			sse = sse.add(responseValue
					.getTargetConcentrationValue()
					.multiply(paramArray[1])
					.add(
							paramArray[0]
							.subtract(responseValue.getResponseCalculationValue())
							).pow(2)
					);
		}
		if (n==0) return Optional.empty();
		
		var sst = ySquare
				.subtract(
						ySum.multiply(ySum, new MathContext(22, RoundingMode.HALF_UP))
						.divide(BigDecimal.valueOf(n))
						);
		
		return Optional.of(BigDecimal.ONE
				.subtract(sse.divide(sst, new MathContext(4, RoundingMode.HALF_UP)))
				.setScale(4, RoundingMode.HALF_UP));
	};
	
	public Optional<String> getFormula(List<BigDecimal> parameters);
	
	public List<BigDecimal> setParameters(List<BigDecimal> parameters);

}
