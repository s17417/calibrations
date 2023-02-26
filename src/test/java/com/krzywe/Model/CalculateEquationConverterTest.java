package com.krzywe.Model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class CalculateEquationConverterTest {
	
	private CalculateEquationConverter converter = new CalculateEquationConverter();
	
	@Test
	public void CalculateEquationConverter_convertToDatabaseColumn() {
		assertThat(converter.convertToDatabaseColumn(new LinearFunctionStrategy()))
		.containsSequence("LINEAR");
	}
	
	@Test
	public void CalculateEquationConverterConvertToDatabaseColumn_NullParameter() {
		assertThat(converter.convertToDatabaseColumn(null))
		.isNull();
	}
	
	@Test
	public void CalculateEquationConverter_convertToEntityAttribute() {
		assertThat(converter.convertToEntityAttribute("LINEAR"))
		.isInstanceOf(LinearFunctionStrategy.class);
	}
	
	@Test
	public void CalculateEquationConverterConvertToEntityAttribute_NullParameter() {
		assertThat(converter.convertToEntityAttribute(null))
		.isNull();
	}
	
	@Test
	public void CalculateEquationConverterConvertToEntityAttribute_UnknownObject() {
		assertThat(converter.convertToEntityAttribute("----"))
		.isNull();
	}

}
