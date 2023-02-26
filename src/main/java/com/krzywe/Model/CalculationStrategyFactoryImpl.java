package com.krzywe.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CalculationStrategyFactoryImpl implements ICalculationStrategyFactory {
	
	private final static ICalculationStrategyFactory CALCULATION_STRATEGY_FACTORY;
	private final static Map<String, IFunctionStrategy> CALCULATION_STRATEGY_MAP;
	private final static Map<Class<? extends IFunctionStrategy>, String> REVERSE_CALCULATION_STRATEGY_MAP;
	
	static {
		CALCULATION_STRATEGY_FACTORY = new CalculationStrategyFactoryImpl();
		CALCULATION_STRATEGY_MAP = Map.of(
				"LINEAR", new LinearFunctionStrategy()
				);
		REVERSE_CALCULATION_STRATEGY_MAP = inverseMap(CALCULATION_STRATEGY_MAP);
	}
	
	private CalculationStrategyFactoryImpl() {};

	@Override
	public Optional<IFunctionStrategy> createCalculationStrategy(String strategy) {
		return strategy==null?Optional.empty():Optional.ofNullable(
				CALCULATION_STRATEGY_MAP.get(strategy)
				);
	}

	@Override
	public Optional<String> getCalculationStrategyName(IFunctionStrategy calculationStrategy) {
		return calculationStrategy==null?Optional.empty():Optional.ofNullable(
				REVERSE_CALCULATION_STRATEGY_MAP.get(calculationStrategy.getClass())
				);
	}
	
	public static ICalculationStrategyFactory getFactory() {
		return CALCULATION_STRATEGY_FACTORY;
	}

	@Override
	public List<String> getObjectNames() {
		return  Arrays.asList(CALCULATION_STRATEGY_MAP.keySet().toArray(new String[0]));
	}
	
	private static <V extends IFunctionStrategy> Map<Class<? extends IFunctionStrategy>, String> inverseMap(Map<String,V> map){
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
