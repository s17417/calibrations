package com.krzywe.Model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public enum WEIGHT implements IWeight {
	
	UNWEIGHTED{
		
		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {			
			return responseValues
					.stream()
					.filter(responseValue -> responseValue.getIsActive())
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue -> BigDecimal.ONE
									)
							);
		}
		
	},
	ONE_BY_Y{

		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			return responseValues
					.stream()
					.filter(responseValue -> responseValue.getIsActive())
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue ->  BigDecimal.ONE.divide(
											responseValue.getResponseCalculationValue().compareTo(BigDecimal.ZERO)==0?
													BigDecimal.ONE:responseValue.getResponseCalculationValue(),32,RoundingMode.HALF_UP)
									)
							);
		}
		
	},
	ONE_BY_Y2{
		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			return responseValues
					.stream()
					.filter(responseValue -> responseValue.getIsActive())
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue ->  BigDecimal.ONE.divide(
											responseValue.getResponseCalculationValue().compareTo(BigDecimal.ZERO)==0?
													BigDecimal.ONE:responseValue.getResponseCalculationValue().pow(2),32,RoundingMode.HALF_UP)
									)
							);
		}
	},
	ONE_BY_X{
		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			return responseValues
					.stream()
					.filter(responseValue -> responseValue.getIsActive())
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue ->  responseValue.getTargetConcentrationValue().compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE : BigDecimal.ONE.divide(responseValue.getTargetConcentrationValue(),32,RoundingMode.HALF_UP)
									)
							);
		}
	},
	ONE_BY_X2{
		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			return responseValues
					.stream()
					.filter(responseValue -> responseValue.getIsActive())
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue -> responseValue.getTargetConcentrationValue().compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE : BigDecimal.ONE.divide(responseValue.getTargetConcentrationValue().pow(2),32,RoundingMode.HALF_UP)
									)
							);
		}
	},
	ONE_BY_RSD{
		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			var groupSumAndQuantityMap = responseValues
					.stream()
					.filter(ResponseValue::getIsActive)
					.collect(
							Collectors.toMap(
									responseValue -> responseValue.getTargetValue(),
									responseValue -> new BigDecimal[]{
											responseValue.getResponseCalculationValue(), //.getResponseValue(),
											BigDecimal.ONE,
											responseValue.getResponseCalculationValue().pow(2) //.getResponseValue().pow(2)
											},
									(arr1, arr2) -> new BigDecimal[] {
											arr1[0].add(arr2[0]),
											arr1[1].add(arr2[1]),
											arr1[2].add(arr2[2])
											}
									)
							);							
			return responseValues
					.stream()
					.filter(ResponseValue::getIsActive)
					.collect(
							Collectors.toMap(
									responseValue -> responseValue,
									responseValue -> { 
										BigDecimal[] arr = groupSumAndQuantityMap.get(responseValue.getTargetValue());
										return arr[1].compareTo(BigDecimal.ONE)==1 ? arr[2]
												.subtract(arr[0].pow(2).divide(arr[1], new MathContext(32, RoundingMode.HALF_UP)))
												.divide(arr[1].subtract(BigDecimal.ONE),new MathContext(32, RoundingMode.HALF_UP))
												.sqrt(new MathContext(32, RoundingMode.HALF_UP))
												: BigDecimal.ONE;
										}
									)
							);
		}		
	},
	ONE_BY_RSD2{

		@Override
		public Map<ResponseValue, BigDecimal> calculateWeights(Collection<ResponseValue> responseValues) {
			var z =WEIGHT.ONE_BY_RSD.calculateWeights(responseValues);
			z.replaceAll((key,value) -> value.pow(2,new MathContext(32, RoundingMode.HALF_UP)));
			return z;			
		}
		
	}
	

}
