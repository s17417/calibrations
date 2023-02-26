package com.krzywe.Model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CalculateEquationConverter implements AttributeConverter<IFunctionStrategy, String> {

	@Override
	public String convertToDatabaseColumn(IFunctionStrategy attribute) {
		return CalculationStrategyFactoryImpl.getFactory()
				.getCalculationStrategyName(attribute)
				.orElse(null);
	}

	@Override
	public IFunctionStrategy convertToEntityAttribute(String dbData) {
		return CalculationStrategyFactoryImpl.getFactory()
				.createCalculationStrategy(dbData)
				.orElse(null);
	}

}
