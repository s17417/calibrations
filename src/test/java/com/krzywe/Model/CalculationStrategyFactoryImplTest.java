package com.krzywe.Model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculationStrategyFactoryImplTest {

	
	private ICalculationStrategyFactory factoryImpl;


	@BeforeEach
	public void beforeEach() {
		factoryImpl = CalculationStrategyFactoryImpl.getFactory();
	}
	
	@Test
	public void checkCalculationStrategyFactoryImpl_getObjectNames() {
		var names = factoryImpl.getObjectNames();
		assertTrue(names!=null);
		assertTrue(names.contains("LINEAR"));
	}
	
	@Test
	public void checkCalculationStrategyFactoryImpl_createCalculationStrategy() {
		var strategy = factoryImpl.createCalculationStrategy("LINEAR");
		assertTrue(strategy.isPresent());
		assertTrue (strategy.get() instanceof LinearFunctionStrategy);
		
		strategy = factoryImpl.createCalculationStrategy("");
		assertTrue(strategy.isEmpty());
	}
	
	@Test
	public void checkCalculationStrategyFactoryImpl_getCalculationStrategyName() {
		var strategy = factoryImpl.getCalculationStrategyName(factoryImpl.createCalculationStrategy("LINEAR").get());
		assertTrue(strategy.isPresent());
		assertTrue(strategy.get().equals("LINEAR"));
	}
}
