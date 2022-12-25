package com.krzywe.Model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CalculateEquationConverter implements AttributeConverter<ICalculateEquation, String> {

	@Override
	public String convertToDatabaseColumn(ICalculateEquation attribute) {
		return attribute
				.getClass()
				.getSimpleName()
				.isEmpty() ?
						null :
							attribute
							.getClass()
							.getSimpleName();
	}

	@Override
	public ICalculateEquation convertToEntityAttribute(String dbData) {
		switch (dbData) {
		case "LinearCalculation" : return new LinearCalculation();
		default : return null;
		}
	}

}
