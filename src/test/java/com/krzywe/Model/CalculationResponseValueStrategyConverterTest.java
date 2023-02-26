package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class CalculationResponseValueStrategyConverterTest {
	
	private CalculationResponseValueStrategyConverter converter = new CalculationResponseValueStrategyConverter();
	
	@Test
	public void CalculationResponseValueStrategyConverter_convertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(new ResponseValueExternalStdTypeImpl()))
		.containsSequence("EXTERNAL_STD");
		
		assertThat(converter.convertToDatabaseColumn(new ResponseValueInternalStdTypeImpl()))
		.containsSequence("INTERNAL_STD");
	}
	
	@Test
	public void CalculationResponseValueStrategyConverterConvertToDatabaseColumn_NullParameter() {
		assertThat(converter.convertToDatabaseColumn(null))
		.isNull();
	}
	
	@Test
	public void CalculationResponseValueStrategy_convertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute("EXTERNAL_STD"))
		.isInstanceOf(ResponseValueExternalStdTypeImpl.class);
		
		assertThat(converter.convertToEntityAttribute("INTERNAL_STD"))
		.isInstanceOf(ResponseValueInternalStdTypeImpl.class);	
	}
	
	@Test
	public void CalculationResponseValueStrategyConvertToEntityAttribute_NullParameter() {
		assertThat(converter.convertToEntityAttribute(null))
		.isNull();
	}
	
	@Test
	public void CalculationResponseValueStrategyConvertToEntityAttribute_UknownObject() {
		assertThat(converter.convertToEntityAttribute("----"))
		.isNull();
	}
	

}
