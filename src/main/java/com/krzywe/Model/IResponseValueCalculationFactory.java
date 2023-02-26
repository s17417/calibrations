package com.krzywe.Model;

import java.util.List;
import java.util.Optional;

public interface IResponseValueCalculationFactory {
	
	public Optional<ICalculationResponseValueStrategy> createCalculationStrategy(String strategy);
	
	public Optional<String> getCalculationStrategyName(ICalculationResponseValueStrategy calculationResponseValueStrategy);
	
	public List<String> getObjectNames();
}


