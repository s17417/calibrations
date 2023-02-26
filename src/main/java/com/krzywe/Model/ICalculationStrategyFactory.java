package com.krzywe.Model;

import java.util.List;
import java.util.Optional;

public interface ICalculationStrategyFactory {
	
	public Optional<IFunctionStrategy> createCalculationStrategy(String strategy);
	
	public Optional<String> getCalculationStrategyName(IFunctionStrategy calculationStrategy);
	
	public List<String> getObjectNames();

}
