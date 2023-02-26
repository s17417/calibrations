package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ResponseValueCalculationFactoryImplTest {

	private IResponseValueCalculationFactory factory = ResponseValueCalculationFactoryImpl.getFactory();
	
	@ParameterizedTest
	@ValueSource(strings = {"EXTERNAL_STD", "INTERNAL_STD"})
	public void responseValueCalculationFactory_createCalculationStrategy(String strategyName) {
		var strategy = factory.createCalculationStrategy(strategyName);
		
		if (strategyName=="EXTERNAL_STD")
			assertThat(strategy).isInstanceOf(ResponseValueExternalStdTypeImpl.class);
		if(strategyName=="INTERNAL_STD")
			assertThat(strategy).isInstanceOf(ResponseValueInternalStdTypeImpl.class);
	}
	
	@Test
	public void responseValueCalculationFactoryCreateCalculationStrategy_NullParameter() {
		assertThat(factory.createCalculationStrategy(null))
		.isEmpty();
	}
	
	@Test
	public void responseValueCalculationFactoryCreateCalculationStrategy_UnknownObject() {
		assertThat(factory.createCalculationStrategy("----"))
		.isEmpty();
	}
	
	@Test
	public void responseValueCalculationFactory_getCalculationStrategyName() {
		assertThat(factory.getCalculationStrategyName(new ResponseValueExternalStdTypeImpl()))
		.isPresent()
		.containsSame("EXTERNAL_STD");
		
		assertThat(factory.getCalculationStrategyName(new ResponseValueInternalStdTypeImpl()))
		.isPresent()
		.containsSame("INTERNAL_STD");
	}
	
	@Test
	public void responseValueCalculationFactoryGetCalculationStrategyName_NullParameter() {
		assertThat(factory.getCalculationStrategyName(null))
		.isEmpty();
	}


	
}
