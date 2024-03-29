package com.krzywe.Model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CalculationResponseValueStrategyConverter
		implements AttributeConverter<ICalculationResponseValueStrategy, String> {

	@Override
	public String convertToDatabaseColumn(ICalculationResponseValueStrategy attribute) {
		return ResponseValueCalculationFactoryImpl.getFactory()
				.getCalculationStrategyName(attribute)
				.orElse(null);
	}

	@Override
	public ICalculationResponseValueStrategy convertToEntityAttribute(String dbData) {
		return ResponseValueCalculationFactoryImpl.getFactory()
				.createCalculationStrategy(dbData).
				orElse(null);
	}

}
