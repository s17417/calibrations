package com.krzywe.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResponseValueCalculationFactoryImpl implements IResponseValueCalculationFactory {
	
	private final static IResponseValueCalculationFactory RESPONSE_VALUE_CALCULATION_FACTORY;
	private final static Map<String, ICalculationResponseValueStrategy> RESPONSE_VALUE_CALCULATION_STRATEGY_MAP;
	private final static Map<Class<? extends ICalculationResponseValueStrategy>, String> REVERSE_RESPONSE_VALUE_CALCULATION_STRATEGY_MAP;

	static {
		RESPONSE_VALUE_CALCULATION_FACTORY = new ResponseValueCalculationFactoryImpl();
		RESPONSE_VALUE_CALCULATION_STRATEGY_MAP = Map.of(
					"EXTERNAL_STD", new ResponseValueExternalStdTypeImpl(),
					"INTERNAL_STD", new ResponseValueInternalStdTypeImpl()
				);
		REVERSE_RESPONSE_VALUE_CALCULATION_STRATEGY_MAP = inverseMap(RESPONSE_VALUE_CALCULATION_STRATEGY_MAP);
	}
	
	private ResponseValueCalculationFactoryImpl() {}
	
	@Override
	public Optional<ICalculationResponseValueStrategy> createCalculationStrategy(String strategy) {
		return strategy==null? Optional.empty():Optional.ofNullable(
				RESPONSE_VALUE_CALCULATION_STRATEGY_MAP.get(strategy)
				);
	}

	@Override
	public Optional<String> getCalculationStrategyName(ICalculationResponseValueStrategy calculationResponseValueStrategy) {	
		return calculationResponseValueStrategy==null? Optional.empty():Optional.ofNullable(
				REVERSE_RESPONSE_VALUE_CALCULATION_STRATEGY_MAP.get(calculationResponseValueStrategy.getClass())
				);
	}

	public static IResponseValueCalculationFactory getFactory() {
			return ResponseValueCalculationFactoryImpl.RESPONSE_VALUE_CALCULATION_FACTORY;
	}
	
	@Override
	public List<String> getObjectNames() {
		return Arrays.asList(RESPONSE_VALUE_CALCULATION_STRATEGY_MAP.keySet().toArray(new String[0]));
	}
	
	private static <V extends ICalculationResponseValueStrategy> Map<Class<? extends ICalculationResponseValueStrategy>, String> inverseMap(Map<String,V> map){
		return map
				.entrySet()
				.stream()
				.collect(
						Collectors
						.toUnmodifiableMap(
								entry -> entry.getValue().getClass(),
								Entry::getKey,
								(obj1, obj2) -> obj1)
						);
	}
	

}
